package com.project.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.Model.Category;
import com.project.Repository.CategoryRepo;
import com.project.Utility.NoCategoryFoundException;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

   
    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    
    /*
     * To get all categories from the database
     * Returns an empty list if no categories are present
     */
    public List<Category> getAllCategories(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return categoryRepo.findAll(pageable).getContent();
        } catch (Exception e) { 
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /*
     * To get a category by ID
     * Throws exception if no category is found
     */
    public Category getCategoryById(int id) throws NoCategoryFoundException {
        return categoryRepo.findById(id)
            .orElseThrow(() -> new NoCategoryFoundException("No category found with ID: " + id));
    }

    /*
     * To create a new category
     */
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    /*
     * To update an existing category
     * Throws exception if no category is found with the given ID
     */
    public Category updateCategory(int id, Category category) throws NoCategoryFoundException {
        if (!categoryRepo.existsById(id)) {
            throw new NoCategoryFoundException("No category found with ID: " + id);
        }
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        return categoryRepo.save(existingCategory);
    }

    /*
     * To delete a category by ID
     * Throws exception if no category is found
     */
    public void deleteCategory(int id) throws NoCategoryFoundException {
        if (!categoryRepo.existsById(id)) {
            throw new NoCategoryFoundException("No category found with ID: " + id);
        }
        categoryRepo.deleteById(id);
    }
}
