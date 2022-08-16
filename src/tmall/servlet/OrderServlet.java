package tmall.servlet;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Order;
import tmall.dao.OrderDAO;
import tmall.util.Page;

public class OrderServlet extends BaseBackServlet {

	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}
	
	/**
	 * 当订单状态是waitDelivery的时候，就会出现发货按钮、
	 * 1. 发货按钮链接跳转到admin_order_delivery，OrderServlet.delivery()方法被调用
	 * 2.根据id获取Order对象
	 * 3.修改发货时间，设置发货状态:waitConfirm
	 * 4.更新到数据库
	 * 5. 客户端跳转到admin_order_list页面
	 * */
	public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Order o = orderDAO.get(id);
		o.setDeliveryDate(new Date());
		o.setStatus(OrderDAO.waitConfirm);
		orderDAO.update(o);
		return "@admin_order_list";
	}

	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;	
	}

	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	/**
	 * 点击"订单管理"或者访问网址http://127.0.0.1:8081/tmall/admin_order_list
	 * admin_order_list 导致OrderServlet.list()方法被调用
	 * 1. 分页查询订单信息
	 * 2. 借助orderItemDAO.fill()方法为这些订单填充上orderItems信息
	 * 3. 服务端跳转到admin/listOrder.jsp页面
	 * 4. 在listOrder.jsp借助c:forEach把订单集合遍历出来 <c:forEach items="${os}" var="o">
	 * 5. 遍历订单的时候，再把当前订单的orderItem订单项集合遍历出来    <c:forEach items="${o.orderItems}" var="oi">
	 * */
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Order> os = orderDAO.list(page.getStart(),page.getCount());
		orderItemDAO.fill(os);
		int total = orderDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("os", os);
		request.setAttribute("page", page);
		
		return "admin/listOrder.jsp";
	}
}
