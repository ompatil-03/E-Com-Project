package com.project.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.Model.Category;
import com.project.Model.Product;
import com.project.Model.ProductDTO;
import com.project.Service.CategoryService;
import com.project.Service.ProductService;
import com.project.Utility.NoCategoryFoundException;
import com.project.Utility.NoProductFoundException;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;    
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }
    
    @ExceptionHandler(NoProductFoundException.class)
    public String handleNoProductFoundException(NoProductFoundException ex) {
        return ex.getMessage();
    }

    @GetMapping
    public List<ProductDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Product> products = productService.getAllProducts(page, size);
            /*
             * following line  converts a list of Product entities into a list of ProductDTO 
             * objects using a constructor reference and then returns the list.
             */
            return products.stream().map(ProductDTO::new).collect(Collectors.toList());
        } catch (Exception e) {          
            System.err.println("Error retrieving products: " + e.getMessage());
            return List.of(); 
        }
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) throws NoProductFoundException, NoCategoryFoundException {
        System.out.println("Product from controller: " + product);
        System.out.println(product.getCategory());
        
        if (product.getCategory() == null || product.getCategory().getId() == 0) {
            return ResponseEntity.badRequest().body(null); // return bad request if category is missing
        }

        int categoryId = product.getCategory().getId();
        Category category;

        try {
            category = categoryService.getCategoryById(categoryId);
        } catch (NoCategoryFoundException e) {
            return ResponseEntity.notFound().build(); // return not found if category doesn't exist
        }

        product.setCategory(category);        
        Product savedProduct = productService.createProduct(product, categoryId);

        // Add the new product to the category's product list
        if (category.getProducts() != null) {
            category.getProducts().add(savedProduct);
        } else {
            category.setProducts(new ArrayList<>(List.of(savedProduct)));
        }

        categoryService.updateCategory(categoryId, category);

        return ResponseEntity.ok(new ProductDTO(savedProduct)); // return created product DTO
    }


    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable int id) throws NoProductFoundException {
        Product product = productService.getProductById(id);
        /*
         * here we used DTO to get desired output
         */
        return new ProductDTO(product); 
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
        	Product temp= productService.updateProduct(id, product);
            return new ProductDTO(temp);
        } catch (Exception e) {
            System.out.println(e);
            return new ProductDTO(); 
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}