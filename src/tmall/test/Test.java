package tmall.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;

public class Test {

	public static void main(String[] args) throws IOException {
		List<Category> cs = new CategoryDAO().list();
		for (Category c : cs) {
			List<Product> ps= new ProductDAO().list(c.getId(),0,Short.MAX_VALUE);
			for (Product p : ps) {
				List<ProductImage> pis1=new ProductImageDAO().list(p, ProductImageDAO.type_single);
				for (ProductImage pi : pis1) {
//					String fileNameFormat = "D:/project/tmall_original/web/img/productSingle_small/%d.jpg";
//					String srcFileName = String.format(fileNameFormat, pi.getId());
//					String destFileName = StringUtils.replace(srcFileName, "tmall_original", "tmall");
//					FileUtils.copyFile(new File(srcFileName), new File(destFileName));
				}
				List<ProductImage> pis2=new ProductImageDAO().list(p, ProductImageDAO.type_detail);
				for (ProductImage pi : pis2) {
					String fileNameFormat = "D:/project/tmall_original/web/img/productDetail/%d.jpg";
					String srcFileName = String.format(fileNameFormat, pi.getId());
					String destFileName = StringUtils.replace(srcFileName, "tmall_original", "tmall");
					FileUtils.copyFile(new File(srcFileName), new File(destFileName));

				}				
//				new ProductDAO().delete(p.getId());
			}
		}
		
//		 new ProductDAO().list();
	}
}

