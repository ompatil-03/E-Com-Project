package com.project.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.Model.Category;
import com.project.Model.Product;
import com.project.Repository.CategoryRepo;
import com.project.Repository.ProductRepo;
import com.project.Utility.NoCategoryFoundException;
import com.project.Utility.NoProductFoundException;

@Service
public class ProductService {
	
	private final ProductRepo productRepo;	
	private final CategoryRepo categoryRepo;
	
	@Autowired
    CategoryService categoryService;

    public ProductService(ProductRepo productRepository,CategoryRepo categoryRepo) {
        this.productRepo = productRepository;
		this.categoryRepo = categoryRepo;
    }

    /*
     * To get All Products from database 
     * Throws exception if no product is present
    */
    public List<Product> getAllProducts(int page, int size) {
    	 try {
	            Pageable pageable = PageRequest.of(page, size);
	            return productRepo.findAll(pageable).getContent();
	        } catch (Exception e) { 
	            e.printStackTrace();
	            return Collections.emptyList();
	        }
    }

    
    /*
     * To get Products by id
     * Throws exception if no product is found
     */
    public Product getProductById(int id) throws NoProductFoundException {
        Product product = productRepo.findById(id)
            .orElseThrow(() -> new NoProductFoundException("Product not found"));
        System.out.println("Fetched product: " + product);
        if (product.getCategory() != null) {
            System.out.println("Category: " + product.getCategory());
        } else {
            System.out.println("Category not found for product id: " + id);
        }
        product.setCategory(product.getCategory());
        return product;
    }

    
    
    /*
     * To get create new product
     * Throws exception if no category is found for given categoryId
     */
    public Product createProduct(Product product, int categoryId)throws NoCategoryFoundException{
    	
    	if(!categoryRepo.existsById(categoryId)) {
    		throw new NoCategoryFoundException("No category found with id "+categoryId);
    	}
        Category category = categoryService.getCategoryById(categoryId);     
        product.setCategory(category);
        return productRepo.save(product);
    }

    /*
     * To update new product
     * Throws exception if no product is found with given id
     */
    public Product updateProduct(int id, Product product) throws NoProductFoundException {
    	 if (!productRepo.existsById(id)) {
             throw new NoProductFoundException("Cannot update. Product not found with ID: " + id);
         }
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        return productRepo.save(existingProduct);
    }

    /*
     * To delete Products by id
     * Throws exception if no product is found
     */
    public void deleteProduct(int id) throws NoProductFoundException {
        if (!productRepo.existsById(id)) {
            throw new NoProductFoundException("Cannot delete. Product not found with ID: " + id);
        }
        productRepo.deleteById(id);
    }
}
