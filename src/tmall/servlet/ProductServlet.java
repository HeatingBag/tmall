package tmall.servlet;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.Page;

public class ProductServlet extends BaseBackServlet {

	/**
	 * 1. 在listProduct.jsp提交数据的时候，除了提交产品名称，小标题，原价格，优惠价格，库存外还会提交cid
	 * 2. 在ProductServlet中根据获取到的cid,name,subTitle,等参数，创建新的Product对象，并插入到数据库
	 * 3. 客户端跳转到admin_product_list,并带上参数cid
	 * */
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);

		String name = request.getParameter("name");
		String subTitle = request.getParameter("subTitle");
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));

		Product p = new Product();

		p.setCategory(c);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOrignalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);

		productDAO.add(p);
		return "@admin_product_list?cid=" + cid;
	}

	/**
	 * 1. 在ProductServlet的delete方法中获取id
	 * 2. 根据id获取Product对象
	 * 3. 借助productDAO删除这个对象对应的数据
	 * 4. 客户端跳转到admin_product_list，并带上参数cid
	 * */
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		productDAO.delete(id);
		return "@admin_product_list?cid=" + p.getCategory().getId();
	}

	/**
	 * 1. 在ProductServlet的edit方法中，根据id获取Product对象
	 * 2. 把Product对象放在request的 "p" 属性中
	 * 4. 在editProduct.jsp中显示属性名称
	 * <input type="hidden" name="id" value="${p.id}">
	 * <input type="hidden" name="cid" value="${p.category.id}">
	 * 5. 在editProduct.jsp中隐式提供id和cid
	 * */
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		request.setAttribute("p", p);
		return "admin/editProduct.jsp";
	}

	/**
	 * 通过产品管理界面的设置属性，跳到编辑页面，调用ProductServlet的editPropertyValue方法：
	 * 1. 获取参数id
	 * 2. 根据id获取Product对象p
	 * 3. 初始化属性值： propertyValueDAO.init(p)。 因为如果是第一次访问，这些属性值是不存在的。
	 * 4. 根据Product的id，获取产品对应的属性值集合
	 * 5. 属性值集合放在request的 "pvs" 属性上
	 * 6. 服务端跳转到admin/editProductValue.jsp 上
	 * 7. 在editProductValue.jsp上，用c:forEach遍历出这些属性值:<c:forEach items="${pvs}" var="pv">
	 * */
	public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		request.setAttribute("p", p);

		propertyValueDAO.init(p);

		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());

		request.setAttribute("pvs", pvs);

		return "admin/editProductValue.jsp";
	}

	/**
	 * !!修改功能采用的是使用post方式提交ajax的异步调用方式
	 * 6.1 获取pvid
	 * 6.2 获取value
	 * 6.3 基于pvid和value,更新PropertyValue对象
	 * 6.4 返回"%success"
	 * 7. BaseBackServlet根据返回值"%success"，直接输出字符串"success" 到浏览器(%结尾，直接输出字符串)
	 * */
	public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pvid = Integer.parseInt(request.getParameter("pvid"));
		String value = request.getParameter("value");

		PropertyValue pv = propertyValueDAO.get(pvid);
		pv.setValue(value);
		propertyValueDAO.update(pv);
		return "%success";
	}

	/**
	 * 1. 在ProductServlet的update方法中获取cid，id, name,subTitle,price等参数
	 * 2. 根据这些参数创建Product对象
	 * 3. 借助productDAO更新这个对象到数据库
	 * 4. 客户端跳转到admin_product_list，并带上参数cid
	 * */
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);

		int id = Integer.parseInt(request.getParameter("id"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		String subTitle = request.getParameter("subTitle");
		String name = request.getParameter("name");

		Product p = new Product();

		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOrignalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setId(id);
		p.setCategory(c);

		productDAO.update(p);
		return "@admin_product_list?cid=" + p.getCategory().getId();
	}

	/**
	 * 1. 获取分类 cid
	 * 2. 基于cid，获取当前分类下的产品集合
	 * 3. 获取当前分类下的产品总数，并且设置给分页page对象
	 * 4. 拼接字符串"&cid="+c.getId()，设置给page对象的Param值。 因为产品分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid
	 * 5. 把产品集合设置到 request的 "ps" 属性上
	 * 6. 把分类对象设置到 request的 "c" 属性上
	 * 7. 把分页对象设置到 request的 "page" 对象上
	 * 8. 服务端跳转到admin/listProduct.jsp页面
	 * 9. 在listProduct.jsp页面上使用c:forEach 遍历ps集合，并显示
	 * */
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);

		List<Product> ps = productDAO.list(cid, page.getStart(), page.getCount());

		int total = productDAO.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid=" + c.getId());

		request.setAttribute("ps", ps);
		request.setAttribute("c", c);
		request.setAttribute("page", page);

		return "admin/listProduct.jsp";
	}
}
