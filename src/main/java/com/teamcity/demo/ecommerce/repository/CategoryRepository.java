package com.teamcity.demo.ecommerce.repository;

import com.teamcity.demo.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    
    List<Category> findByActiveTrue();
    
    Page<Category> findByActiveTrue(Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.active = true AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Category> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.active = true AND SIZE(c.products) > 0")
    List<Category> findCategoriesWithProducts();
    
    @Query("SELECT COUNT(c) FROM Category c WHERE c.active = true")
    long countActiveCategories();
    
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Long id);
}