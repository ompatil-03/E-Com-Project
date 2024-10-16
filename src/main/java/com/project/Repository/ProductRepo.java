package com.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Model.Product;
@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>{
}
