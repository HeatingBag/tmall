package tmall.servlet;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.PropertyValueDAO;
import tmall.dao.ReviewDAO;
import tmall.dao.UserDAO;
import tmall.util.Page;

public class BaseForeServlet extends HttpServlet {

	protected CategoryDAO categoryDAO = new CategoryDAO();
	protected OrderDAO orderDAO = new OrderDAO();
	protected OrderItemDAO orderItemDAO = new OrderItemDAO();
	protected ProductDAO productDAO = new ProductDAO();
	protected ProductImageDAO productImageDAO = new ProductImageDAO();
	protected PropertyDAO propertyDAO = new PropertyDAO();
	protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
	protected ReviewDAO reviewDAO = new ReviewDAO();
	protected UserDAO userDAO = new UserDAO();

	/**
	 * 4. foreServlet 继承了BaseForeServlet，其service方法会被调用。 
	 * 在service中，借助反射技术，根据传递过来的值 home，调用对应foreServlet 中的方法home()
	 * [[1. 首先ForeServlet继承了BaseForeServlet，而BaseForeServlet又继承了HttpServlet
	 * 2. 服务端跳转过来之后，会访问ForeServlet的doGet()或者doPost()方法
	 * 3. 在访问doGet()或者doPost()之前，会访问service()方法,再决定使用doGet()或者doPost()
	 * 4. BaseForeServlet中重写了service() 方法，所以流程就进入到了service()中
	 * 5. 在service()方法中有三块内容
	 * 5.1 第一块是获取分页信息
	 * 5.2 第二块是根据反射访问对应的方法
	 * 5.3 第三块是根据对应方法的返回值，进行服务端跳转、客户端跳转、或者直接输出字符串。
	 * 6. 第一块：分页信息实际上在前台并没有用到，目前前台并没有提供分页功能。
	 * 7. 第二块：取到从ForeServletFilter中request.setAttribute()传递过来的值 home，
	 * 根据这个值home，借助反射机制调用ForeServlet类中的home()方法，这样就达到了ForeServlet.home()方法被调用的效果
	 * 8. 第三块： 判断根据home的返回值"home.jsp"，即没有"%"开头，也没有"@",那么就调用request.getRequestDispatcher(redirect).forward(request, response);
	 * 进行服务端跳转到 "home.jsp" 页面]]
	 * 5. 这样就实现了当访问的路径是 forehome的时候，就会调用foreServlet.home()方法这样一个效果
	 * 换句话说:
	 * 如果访问的路径是forecategory，就会调用foreServlet.category()方法
	 * 如果访问的路径是forecart，就会调用foreServlet.cart()方法
	 * 如果访问的路径是foresearch，就会调用foreServlet.search()方法
	 * 。。。
	 * */
	public void service(HttpServletRequest request, HttpServletResponse response) {
		try {

			int start = 0;
			int count = 10;
			try {
				start = Integer.parseInt(request.getParameter("page.start"));
			} catch (Exception e) {

			}

			try {
				count = Integer.parseInt(request.getParameter("page.count"));
			} catch (Exception e) {
			}

			Page page = new Page(start, count);

			// BackServletFilter中传递过来的method=home
			String method = (String) request.getAttribute("method");

			// 赤石的this=ForeServlet，因为代码从ForeServletFilter过来后是直接到了ForeServlet，
			// 因为 foreServlet 继承了BaseForeServlet，其service方法会自动被调用，若以这里的this一定是ForeServlet
			Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
					javax.servlet.http.HttpServletResponse.class, Page.class);

			String redirect = m.invoke(this, request, response, page).toString();

			if (redirect.startsWith("@"))
				response.sendRedirect(redirect.substring(1));
			else if (redirect.startsWith("%"))
				response.getWriter().print(redirect.substring(1));
			else
				request.getRequestDispatcher(redirect).forward(request, response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
 
