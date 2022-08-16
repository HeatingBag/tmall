package tmall.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.Page;

public class PropertyServlet extends BaseBackServlet {

	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 在listProperty.jsp页面提交数据的时候，除了提交属性名称，还会提交cid
		 * 2. 在PropertyServlet中根据获取到的cid,name参数，创建新的Property对象，并插入到数据库
		 * 3. 客户端跳转到admin_property_list,并带上参数cid
		 * */
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);

		String name = request.getParameter("name");
		Property p = new Property();
		p.setCategory(c);
		p.setName(name);
		propertyDAO.add(p);
		return "@admin_property_list?cid=" + cid;
	}

	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 在PropertyServlet的delete方法中获取id
		 * 2. 根据id获取Property对象
		 * 3. 借助propertyDAO删除这个对象对应的数据
		 * 4. 客户端跳转到admin_property_list，并带上参数cid
		 * */
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.get(id);
		propertyDAO.delete(id);
		return "@admin_property_list?cid=" + p.getCategory().getId();
	}

	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 在PropertyServlet的edit方法中，根据id获取Property对象
		 * 2. 把Property对象放在request的 "p" 属性中
		 * 3. 服务端跳转到admin/editProperty.jsp
		 * */
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.get(id);
		request.setAttribute("p", p);
		return "admin/editProperty.jsp";
	}

	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 在PropertyServlet的update方法中获取cid，id, name等参数
		 * 2. 根据这些参数创建Property对象
		 * 3. 借助propertyDAO更新这个对象到数据库
		 * 4. 客户端跳转到admin_property_list，并带上参数cid
		 * */
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);

		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		Property p = new Property();
		p.setCategory(c);
		p.setId(id);
		p.setName(name);
		propertyDAO.update(p);
		return "@admin_property_list?cid=" + p.getCategory().getId();
	}

	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 获取分类 cid
		 * 2. 基于cid，获取当前分类下的属性集合
		 * 3. 获取当前分类下的属性总数，并且设置给分页page对象
		 * 4. 拼接字符串"&cid="+c.getId()，设置给page对象的Param值。 因为属性分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid
		 * 5. 把属性集合设置到 request的 "ps" 属性上
		 * 6. 把分类对象设置到 request的 "c" 属性上
		 * 7. 把分页对象设置到 request的 "page" 对象上
		 * 8. 服务端跳转到admin/listProperty.jsp页面(没有@和% 直接跳转)
		 * 9. 在listProperty.jsp页面上使用c:forEach 遍历ps集合，并显示
		 * */
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		List<Property> ps = propertyDAO.list(cid, page.getStart(), page.getCount());
		int total = propertyDAO.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid=" + c.getId());

		request.setAttribute("ps", ps);
		request.setAttribute("c", c);
		request.setAttribute("page", page);

		return "admin/listProperty.jsp";
	}
}

