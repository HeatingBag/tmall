package tmall.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

import tmall.bean.User;
import tmall.util.Page;

public class UserServlet extends BaseBackServlet {

	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

		return null;
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;		
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	/**
	 * 点击后台管理上方导航中的 用户管理，进入用户查询界面，UserServlet的list方法被调用
	 * 1. 借助userDAO查询用户集合
	 * 2. 设置分页信息
	 * 3. 把用户集合设置到request的"us"属性上
	 * 4. 把分页对象设置到request的"page"属性上
	 * 5. 服务端跳转到admin/listUser.jsp页面
	 * 6. 在listUser.jsp用c:forEach遍历"us"集合 <c:forEach items="${us}" var="u">
	 * */
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<User> us = userDAO.list(page.getStart(),page.getCount());
		int total = userDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("us", us);
		request.setAttribute("page", page);
		
		return "admin/listUser.jsp";
	}
}

