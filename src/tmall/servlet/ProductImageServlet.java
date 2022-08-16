package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.ProductImageDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class ProductImageServlet extends BaseBackServlet {

	/**
	 * 增加产品图片分单个和详情两种，其区别在于增加的提交的type类型不一样。
	 * 这里就对单个的进行讲解，详情图片的处理同理。
	 * 首先， 在listProductImage.jsp准备一个form，提交到admin_productImage_add
	 * <form method="post" class="addFormSingle" action="admin_productImage_add" enctype="multipart/form-data">
	 * 接着到了ProductImageServlet.add()方法
	 * 1. parseUpload 获取上传文件的输入流(BaseBackServlet.parseUpload())
	 * 2. parseUpload 方法会修改params 参数，并且把浏览器提交的type,pid信息放在其中(params.put(paramName, paramValue);)
	 * 3. 从params 中取出type,pid信息，并根据这个type,pid，借助productImageDAO，向数据库中插入数据。
	 * 4. 根据request.getSession().getServletContext().getRealPath( "img/productSingle")，定位到存放分类图片的目录
	 * (除了productSingle，还有productSingle_middle和productSingle_small。 因为每上传一张图片，都会有对应的正常，中等和小的三种大小图片，并且放在3个不同的目录下)
	 * 5. 文件命名以保存到数据库的分类对象的id+".jpg"的格式命名
	 * 6. 根据步骤1获取的输入流，把浏览器提交的文件，复制到目标文件
	 * 7. 借助ImageUtil.change2jpg()方法把格式真正转化为jpg，而不仅仅是后缀名为.jpg
	 * 8. 再借助ImageUtil.resizeImage把正常大小的图片，改变大小之后，分别复制到productSingle_middle和productSingle_small目录下。
	 * 9. 处理完毕之后，客户端条跳转到admin_productImage_list?pid=，并带上pid。
	 * */
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// 上传文件的输入流
		InputStream is = null;
		// 提交上传文件时的其他参数
		Map<String, String> params = new HashMap<>();

		// 解析上传
		is = parseUpload(request, params);

		// 根据上传的参数生成productImage对象
		String type = params.get("type");
		int pid = Integer.parseInt(params.get("pid"));
		Product p = productDAO.get(pid);

		ProductImage pi = new ProductImage();
		pi.setType(type);
		pi.setProduct(p);
		productImageDAO.add(pi);

		// 生成文件
		String fileName = pi.getId() + ".jpg";
		String imageFolder;
		String imageFolder_small = null;
		String imageFolder_middle = null;
		if (ProductImageDAO.type_single.equals(pi.getType())) {
			imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
			imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
			imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
		}

		else
			imageFolder = request.getSession().getServletContext().getRealPath("img/productDetail");
		File f = new File(imageFolder, fileName);
		f.getParentFile().mkdirs();

		// 复制文件
		try {
			if (null != is && 0 != is.available()) {
				try (FileOutputStream fos = new FileOutputStream(f)) {
					byte b[] = new byte[1024 * 1024];
					int length = 0;
					while (-1 != (length = is.read(b))) {
						fos.write(b, 0, length);
					}
					fos.flush();
					// 通过如下代码，把文件保存为jpg格式
					BufferedImage img = ImageUtil.change2jpg(f);
					ImageIO.write(img, "jpg", f);

					if (ProductImageDAO.type_single.equals(pi.getType())) {
						File f_small = new File(imageFolder_small, fileName);
						File f_middle = new File(imageFolder_middle, fileName);

						ImageUtil.resizeImage(f, 56, 56, f_small);
						ImageUtil.resizeImage(f, 217, 190, f_middle);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "@admin_productImage_list?pid=" + p.getId();
	}

	/**
	 * 1. 获取id
	 * 2. 根据id获取ProductImage 对象pi
	 * 3. 借助productImageDAO，删除数据
	 * 4. 这里开始分情况，如果是单个图片，ProductImageDAO.type_single.equals(pi.getType())，那么删除3张正常，中等，小号图片
	 * 5. 如果是详情图片，那么删除一张图片
	 * 6. 客户端跳转到admin_productImage_list地址
	 * */
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		ProductImage pi = productImageDAO.get(id);
		productImageDAO.delete(id);

		if (ProductImageDAO.type_single.equals(pi.getType())) {
			String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
			String imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
			String imageFolder_middle = request.getSession().getServletContext()
					.getRealPath("img/productSingle_middle");

			File f_single = new File(imageFolder_single, pi.getId() + ".jpg");
			f_single.delete();
			File f_small = new File(imageFolder_small, pi.getId() + ".jpg");
			f_small.delete();
			File f_middle = new File(imageFolder_middle, pi.getId() + ".jpg");
			f_middle.delete();

		}

		else {
			String imageFolder_detail = request.getSession().getServletContext().getRealPath("img/productDetail");
			File f_detail = new File(imageFolder_detail, pi.getId() + ".jpg");
			f_detail.delete();
		}
		return "@admin_productImage_list?pid=" + pi.getProduct().getId();
	}

	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	/**
	 * 1. 获取参数pid
	 * 2. 根据pid获取Product对象
	 * 3. 根据product对象获取单个图片的集合pisSingle
	 * 4. 根据product对象获取详情图片的集合pisDetail
	 * 5. 把product 对象，pisSingle ，pisDetail放在request上
	 * 6. 服务端跳转到admin/listProductImage.jsp页面
	 * <c:forEach items="${pisSingle}" var="pi">
	 * <c:forEach items="${pisDetail}" var="pi">
	 * 7. 在listProductImage.jsp，使用c:forEach 遍历pisSingle，pisDetail
	 * */
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		List<ProductImage> pisSingle = productImageDAO.list(p, ProductImageDAO.type_single);
		List<ProductImage> pisDetail = productImageDAO.list(p, ProductImageDAO.type_detail);

		request.setAttribute("p", p);
		request.setAttribute("pisSingle", pisSingle);
		request.setAttribute("pisDetail", pisDetail);

		return "admin/listProductImage.jsp";
	}
}
