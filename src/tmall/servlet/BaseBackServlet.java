package tmall.servlet;

import java.io.InputStream;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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

public abstract class BaseBackServlet extends HttpServlet {

	public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);

	public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);

	public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);

	public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);

	public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

	protected CategoryDAO categoryDAO = new CategoryDAO();
	protected OrderDAO orderDAO = new OrderDAO();
	protected OrderItemDAO orderItemDAO = new OrderItemDAO();
	protected ProductDAO productDAO = new ProductDAO();
	protected ProductImageDAO productImageDAO = new ProductImageDAO();
	protected PropertyDAO propertyDAO = new PropertyDAO();
	protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
	protected ReviewDAO reviewDAO = new ReviewDAO();
	protected UserDAO userDAO = new UserDAO();

	public void service(HttpServletRequest request, HttpServletResponse response) {
		try {

			/*获取分页信息*/
			int start = 0;
			int count = 5;
			try {
				start = Integer.parseInt(request.getParameter("page.start"));
			} catch (Exception e) {

			}
			try {
				count = Integer.parseInt(request.getParameter("page.count"));
			} catch (Exception e) {
			}
			Page page = new Page(start, count);

			/*借助反射，调用对应的方法*/
			/*假设访问路径是 http://127.0.0.1:8080/tmall/admin_category_list，代码到了这里*/

			// 在BackServletFilter中有一个request.setAttribute("method",
			// method);，这里就把method中的"method"也就是list取出来了
			// 此时也就是method=list
			String method = (String) request.getAttribute("method");

			// 这里就是反射，得到了一个方法对象，此时的m---->CategoryServlet.list()方法
			Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
					javax.servlet.http.HttpServletResponse.class, Page.class);

			// m.invoke也就是调用这个(CategoryServlet.list())方法(这里的this指当前对象)
			// 那么就从最开始访问http://127.0.0.1:8080/tmall/admin_category_list，导致了CategoryServlet.list()被调用
			String redirect = m.invoke(this, request, response, page).toString();

			/*根据方法的返回值，进行相应的客户端跳转，服务端跳转，或者仅仅是输出字符串*/
			// 到了CategoryServlet.list()方法会返回一个字符串return
			// "admin/listCategory.jsp";那么此时redirect="admin/listCategory.jsp";
			// 如果redirect是以@开头的，就客户端跳转(substring(1):截取从第1个开始的字符串(基0))
			if (redirect.startsWith("@"))
				response.sendRedirect(redirect.substring(1));
			// 如果redirect是以%开头的，就把返回的字符串直接输出出去getWriter().print()
			else if (redirect.startsWith("%"))
				response.getWriter().print(redirect.substring(1));
			// 如果两者都不是，既不是@也不是%，就进行服务端跳转getRequestDispatcher(redirect).forward(request,
			// response);
			// 就达到了访问CategoryServlet.list()，返回一个字符串"admin/listCategory.jsp"，就直接跳转到了listCategory.jsp的效果
			else
				request.getRequestDispatcher(redirect).forward(request, response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
		InputStream is = null;
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置上传文件的大小限制为10M
			factory.setSizeThreshold(1024 * 10240);

			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					// item.getInputStream() 获取上传文件的输入流
					is = item.getInputStream();
				} else {
					String paramName = item.getFieldName();
					String paramValue = item.getString();
					paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
					// 把浏览器提交的name信息放在params中
					params.put(paramName, paramValue);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

}
