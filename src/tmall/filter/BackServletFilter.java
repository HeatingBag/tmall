package tmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class BackServletFilter implements Filter {

	public void destroy() {

	}

	/**
	 * 假设访问路径是 http://127.0.0.1:8080/tmall/admin_category_list
	 * 首先所有的访问都会经过过滤器：
	 * <filter-mapping>
	 * <filter-name>BackServletFilter</filter-name>
	 *  <url-pattern>/*</url-pattern>
	 *  </filter-mapping>
	 *  那个就到了BackServletFilter
	 * */

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		/*首先把ServletRequest转换成为HttpServletRequest，HttpServletRequest为ServletRequest的子类，所以可以强转*/
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// 此时的contextPath就是/tmall
		String contextPath = request.getServletContext().getContextPath();

		// 此时的uri就是/tmall/admin_category_list
		String uri = request.getRequestURI();

		// 然后StringUtils.remove(uri, contextPath);就是去掉/tmall，就得到了/admin_category_list
		uri = StringUtils.remove(uri, contextPath);

		// 判断是否是/admin_开头的
		if (uri.startsWith("/admin_")) {
			// 此时的servletPath就是取出了两个下划线之间的字符串，现在也就是category，再加上Servlet，那么此时的servletPath=categoryServlet
			String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet";

			// substringAfterLast也就是最后面的一个，这里的意思就是取最后一个_后面的，那么此时的method=list
			String method = StringUtils.substringAfterLast(uri, "_");

			// 把request.setAttribute("method", list);，把list放到method属性上面去了
			request.setAttribute("method", method);

			/**
			 *进行服务端跳转，req.getRequestDispatcher("/categoryServlet").forward(request, response);到categoryServlet去了
			 *
			 *根据web.xml的配置：<servlet-name>CategoryServlet</servlet-name>
			 *                <url-pattern>/categoryServlet</url-pattern>
			 *就会服务端跳转到tmall.servlet.CategoryServlet
			 * */
			req.getRequestDispatcher("/" + servletPath).forward(request, response);
			return;
		}

		// 如果不是以/admin_开头的就直接放行。不做处理
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}
}

