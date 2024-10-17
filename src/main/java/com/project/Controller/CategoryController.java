package com.project.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.Model.Category;
import com.project.Model.CategoryDTO;
import com.project.Service.CategoryService;
import com.project.Utility.NoCategoryFoundException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
		/*
		 * Dependency injection using constructor
		 */
	 	private final CategoryService categoryService;
	    public CategoryController(CategoryService categoryService) {
	        this.categoryService = categoryService;
	    }
	    
	    /*
	     * Mapping to get all categories
	     */
	    @GetMapping
	    public ResponseEntity<List<CategoryDTO>> getAllCategories(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        List<Category> categories = categoryService.getAllCategories(page, size);
	        //line checks if category is empty or not 
	        if (categories.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }        
	        /*
	         * here we convert list<Categroy> to list<CategoryDTO> to show output in desired manner and 
	         *avoiding the circular reference while showing the result
	         */
	        List<CategoryDTO> categoryDTOs = categories.stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	        
	        return ResponseEntity.ok(categoryDTOs);
	    }
	    

	   /*
	    * Mapping to create new Category
	    */
	    @PostMapping
	    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
	       try {
	    	   return ResponseEntity.ok(categoryService.createCategory(category));
	       }catch(Exception e) {
	    	   System.out.println(e);
	    	   return ResponseEntity.badRequest().build();
	       }
	    }

	    /*
	     * This mapping is to get category by id 
	     * and here we used DTO to get output in desired manner 
	     */
	    @GetMapping("/{id}")
	    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int id) {
	        try {
	            Category category = categoryService.getCategoryById(id);
	            return ResponseEntity.ok(new CategoryDTO(category));
	        } catch (Exception e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable int id, @RequestBody Category category) throws NoCategoryFoundException{
	       try {
	    	   Category temp=categoryService.updateCategory(id, category);
		    	return ResponseEntity.ok(new CategoryDTO(temp));
	       }catch(Exception e) {
	    	   System.out.println(e);
	    	   return ResponseEntity.notFound().build();
	       }
	    }

	    @DeleteMapping("/{id}")
	    public void deleteCategory(@PathVariable int id) {
	        try {
				categoryService.deleteCategory(id);
			} catch (NoCategoryFoundException e) {

				e.printStackTrace();
			}
	    }
	    
	    
	    /*
	     * Method to convert entity to DTO type 
	     */
	    private CategoryDTO convertToDTO(Category category) {
	        CategoryDTO dto = new CategoryDTO();
	        dto.setId(category.getId());
	        dto.setName(category.getName());
	        return dto;
	    }
}
