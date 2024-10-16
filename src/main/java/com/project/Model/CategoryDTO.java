package com.project.Model;

public class CategoryDTO {
	  private int id;
	    private String name;
		public CategoryDTO(int id,String name) {
			super();
			this.id=id;
			this.name = name;
		}
		public CategoryDTO(Category cat) {
			this.id=cat.getId();
			this.name=cat.getName();
		}
		public CategoryDTO() {
			super();
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
		@Override
		public String toString() {
			return "CategoryDTO [id=" + id + ", name=" + name + "]";
		}
		
	    
}
