package tmall.bean;

import java.util.List;

public class Category {

	/**
	 * 基本属性id,name，以及一对多关系 products的 getter与setter。
	 * */
	private String name;
	private int id;
	List<Product> products;
	List<List<Product>> productsByRow;
	
	/**
	 * productsByRow这个属性的类型是List<List<Product>> productsByRow。
	 * 即一个分类又对应多个 List<Product>，提供这个属性，是为了在首页竖状导航的分类名称右边显示产品列表。
	 * 一个分类会对应多行产品，而一行产品里又有多个产品记录,为了实现界面上的这个功能，为Category类设计了List<List<Product>> productsByRow这样一个集合属性
	 * */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + "]";
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<List<Product>> getProductsByRow() {
		return productsByRow;
	}

	public void setProductsByRow(List<List<Product>> productsByRow) {
		this.productsByRow = productsByRow;
	}

}
