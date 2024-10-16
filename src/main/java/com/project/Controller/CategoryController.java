package com.project.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	 @Autowired
	 private final CategoryService categoryService;

	    public CategoryController(CategoryService categoryService) {
	        this.categoryService = categoryService;
	    }
	    
	    @GetMapping
	    public ResponseEntity<List<CategoryDTO>> getAllCategories(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        List<Category> categories = categoryService.getAllCategories(page, size);
	        
	        if (categories.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }        
	        
	        List<CategoryDTO> categoryDTOs = categories.stream()
	                .map(this::convertToDTO)
	                .collect(Collectors.toList());
	        
	        return ResponseEntity.ok(categoryDTOs);
	    }


	    @PostMapping
	    public Category createCategory(@RequestBody Category category) {
	        return categoryService.createCategory(category);
	    }

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
	    public CategoryDTO updateCategory(@PathVariable int id, @RequestBody Category category) throws NoCategoryFoundException{
	        Category temp=categoryService.updateCategory(id, category);
	        System.out.println(temp);
	    	return new CategoryDTO(temp);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteCategory(@PathVariable int id) {
	        try {
				categoryService.deleteCategory(id);
			} catch (NoCategoryFoundException e) {

				e.printStackTrace();
			}
	    }
	    
	    private CategoryDTO convertToDTO(Category category) {
	        CategoryDTO dto = new CategoryDTO();
	        dto.setId(category.getId());
	        dto.setName(category.getName());
	        return dto;
	    }
}
