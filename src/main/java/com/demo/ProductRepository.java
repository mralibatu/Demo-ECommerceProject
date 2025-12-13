package com.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByNameContaining(String name);
    
    @Query("SELECT p FROM Product p WHERE p.quantity < ?1")
    List<Product> findLowStockProducts(Integer threshold);
}