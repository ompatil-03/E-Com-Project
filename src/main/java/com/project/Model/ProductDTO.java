package com.project.Model;

public class ProductDTO {
	
	    private int id;
	    private String name;
	    private double price;
	    private CategoryDTO category;
	    
	    
	    public ProductDTO(Product product) {
	        this.id = product.getId();
	        this.name = product.getName();
	        this.price = product.getPrice();
	        if (product.getCategory() != null) {
	            this.category = new CategoryDTO(product.getCategory().getId(), product.getCategory().getName());
	        }
	    }

		public ProductDTO() {
			super();
			// TODO Auto-generated constructor stub
		}


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


		public double getPrice() {
			return price;
		}


		public void setPrice(double price) {
			this.price = price;
		}


		public CategoryDTO getCategory() {
			return category;
		}


		public void setCategory(CategoryDTO category) {
			this.category = category;
		}


		@Override
		public String toString() {
			return "ProductDTO [id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + "]";
		}
	    
	    
}
