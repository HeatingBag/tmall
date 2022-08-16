package tmall.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;

public class ForeServletFilter implements Filter{
	
	@Override
	public void destroy() {
		
	}

	/**
	 * 1.假定现在访问路径是 http://127.0.0.1:8081/tmall/forehome
	 * <filter-name>ForeServletFilter</filter-name>
	 * <dispatcher>FORWARD</dispatcher>
	 * <dispatcher>REQUEST</dispatcher>  
	 *  <url-pattern>/*</url-pattern>
	 * 代码经过过滤器(web.xml)后会到ForeServletFilter
	 * */
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		/*首先把ServletRequest转换成为HttpServletRequest，HttpServletRequest为ServletRequest的子类，所以可以强转*/
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		//此时的contextPath就是/tmall
		String contextPath=request.getServletContext().getContextPath();
		//把参数contextPath传到request中
		request.getServletContext().setAttribute("contextPath", contextPath);
		
		User user =(User) request.getSession().getAttribute("user");
		int cartTotalItemNumber= 0;
		if(null!=user){
			List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
			for (OrderItem oi : ois) {
				cartTotalItemNumber+=oi.getNumber();
			}
		}
		request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
		
		List<Category> cs=(List<Category>) request.getAttribute("cs");
		if(null==cs){
			cs=new CategoryDAO().list();
			request.setAttribute("cs", cs);			
		}
		
		// 此时的uri就是/tmall/forehome
		String uri = request.getRequestURI();
		//// 然后StringUtils.remove(uri, contextPath);就是去掉/tmall，就得到了/forehome
		uri =StringUtils.remove(uri, contextPath);
		/**
		 * 2. 过滤器ForeServletFilter进行拦截，判断访问的地址是否以/fore开头,并且不以/foreServlet开头. 为什么要排除/foreServlet呢？
		 * 因为/foreServlet是web.xml中实际配置的Servlet，filter需要对这个路径访问放行。
		 * 3. 如果是，那么做如下操作
		 * 3.1 取出fore之后的值home
		 * 3.2 服务端跳转到foreServlet，并且把home这个值传递过去
		 * */
		if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet")){
			//substringAfterLast意为去掉最后面一个，这时候method=home
			String method = StringUtils.substringAfterLast(uri,"/fore" );
			
			// 把request.setAttribute("method", home);，把home放到method属性上面去了
			request.setAttribute("method", method);
			
			req.getRequestDispatcher("/foreServlet").forward(request, response);
			return;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	
}


