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

import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

/**
 * 假设访问路径是 http://127.0.0.1:8080/tmall/admin_category_list
 * 经过了BackServletFilter后跳转到CategoryServlet，到了CategoryServlet肯定会访问service方法
 * service再决定是掉用doGet还是doPost
 * CategoryServlet继承了BaseBackServlet，而BaseBackServlet重写了service
 * */

public class CategoryServlet extends BaseBackServlet {

	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// params获取参数
		Map<String, String> params = new HashMap<>();

		// 这里用super表示它是父类的一个方法，在BaseBackServlet中parseUpload(),返回一个输入流
		InputStream is = super.parseUpload(request, params);

		// 根据Map<> params中取出名字，创建对象，并上传到数据库
		// 从params 中取出name信息，并根据这个name信息，借助categoryDAO，向数据库中插入数据。
		String name = params.get("name");
		Category c = new Category();
		c.setName(name);
		categoryDAO.add(c);

		// 创建图片目录(getRealPath,相对路径，基于现在的，会直接定位到web，再加上img/category
		File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
		// 生产文件名,文件命名以保存到数据库的分类对象的id+".jpg"的格式命名
		File file = new File(imageFolder, c.getId() + ".jpg");

		try {
			if (null != is && 0 != is.available()) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					byte b[] = new byte[1024 * 1024];
					int length = 0;
					while (-1 != (length = is.read(b))) {
						// 根据parseUpload 获取的输入流is，把浏览器提交的文件，复制到目标文件
						fos.write(b, 0, length);
					}
					fos.flush();
					// 通过如下代码，把文件保存为jpg格式,借助ImageUtil.change2jpg()方法把格式真正转化为jpg，而不仅仅是后缀名为.jpg
					BufferedImage img = ImageUtil.change2jpg(file);
					ImageIO.write(img, "jpg", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "@admin_category_list";
	}

	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 获取id
		 * 2. 借助categoryDAO.delete()方法删除本id对应数据(数据库中的数据)
		 * 3. 客户端跳转到admin_category_list路径
		 * */
		int id = Integer.parseInt(request.getParameter("id"));
		categoryDAO.delete(id);
		return "@admin_category_list";
	}

	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. 获取id
		 * 2. 借助categoryDAO，根据id获取Category对象
		 * 3. 把Category对象放在request里
		 * 4. 服务端跳转到admin/editCategory.jsp 页面
		 * */
		int id = Integer.parseInt(request.getParameter("id"));
		Category c = categoryDAO.get(id);
		request.setAttribute("c", c);
		return "admin/editCategory.jsp";
	}

	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		/**
		 * 1. parseUpload 获取上传文件的输入流
		 * 2. parseUpload 方法会修改params 参数，并且把浏览器提交的name信息放在其中
		 * 3. 从params 中取出id和name信息，并根据这个id,name信息，创建新的Category对象，并借助categoryDAO，向数据库中更新数据。
		 * 4. 根据request.getServletContext().getRealPath( "img/category")，定位到存放分类图片的目录
		 * 5. 文件命名以保存到数据库的分类对象的id+".jpg"的格式命名
		 * 6. 如果通过parseUpload 获取到的输入流是空的，或者其中的可取字节数为0，那么就不进行上传处理
		 * if(null!=is && 0!=is.available())
		 * 7. 根据步骤1获取的输入流，把浏览器提交的文件，复制到目标文件
		 * 8. 借助ImageUtil.change2jpg()方法把格式真正转化为jpg，而不仅仅是后缀名为.jpg
		 * 9. 最后客户端跳转到admin_category_list
		 * */
		Map<String, String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);
		String name = params.get("name");
		int id = Integer.parseInt(params.get("id"));

		Category c = new Category();
		c.setId(id);
		c.setName(name);
		categoryDAO.update(c);

		File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, c.getId() + ".jpg");
		// 创建文件
		file.getParentFile().mkdirs();

		try {
			if (null != is && 0 != is.available()) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					byte b[] = new byte[1024 * 1024];
					int length = 0;
					while (-1 != (length = is.read(b))) {
						fos.write(b, 0, length);
					}
					fos.flush();
					// 通过如下代码，把文件保存为jpg格式
					BufferedImage img = ImageUtil.change2jpg(file);
					ImageIO.write(img, "jpg", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "@admin_category_list";

	}

	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// 首先把数据取出来，会带上分页信息，得到一个装有分类对象Category的集合cs
		List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());

		// 取到分类的总数，主要也是为了给分类用的
		int total = categoryDAO.getTotal();
		page.setTotal(total);

		// 把数据传过去
		request.setAttribute("thecs", cs);
		request.setAttribute("page", page);

		// 跳转到admin/listCategory.jsp(返回一个字符串就调整详细作用原理在BaseBackServlet)
		return "admin/listCategory.jsp";
	}
}
