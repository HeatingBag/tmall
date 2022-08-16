package tmall.bean;

public class ProductImage {
	
	/**
	 * 基础属性id,type
	 * 与Product的多对一关系
	 * */
	private String type;
	private Product product;
	private int id;

	public int getId() {
		return id;
	}
	

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
