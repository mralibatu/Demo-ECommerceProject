package com.teamcity.demo.ecommerce.service;

import com.teamcity.demo.ecommerce.dto.CategoryDto;
import com.teamcity.demo.ecommerce.entity.Category;
import com.teamcity.demo.ecommerce.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        logger.debug("Fetching all active categories with pagination: {}", pageable);
        return categoryRepository.findByActiveTrue(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategoriesAsList() {
        logger.debug("Fetching all active categories as list");
        return categoryRepository.findByActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        logger.debug("Fetching category by id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return convertToDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryByName(String name) {
        logger.debug("Fetching category by name: {}", name);
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
        return convertToDto(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> searchCategories(String searchTerm, Pageable pageable) {
        logger.debug("Searching categories with term: {} and pagination: {}", searchTerm, pageable);
        return categoryRepository.findBySearchTerm(searchTerm, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesWithProducts() {
        logger.debug("Fetching categories that have products");
        return categoryRepository.findCategoriesWithProducts()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        logger.info("Creating new category with name: {}", categoryDto.getName());
        
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category category = convertToEntity(categoryDto);
        category = categoryRepository.save(category);
        logger.info("Created category with id: {}", category.getId());
        
        return convertToDto(category);
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        logger.info("Updating category with id: {}", id);
        
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (categoryRepository.existsByNameAndIdNot(categoryDto.getName(), id)) {
            throw new IllegalArgumentException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());
        if (categoryDto.getActive() != null) {
            existingCategory.setActive(categoryDto.getActive());
        }

        existingCategory = categoryRepository.save(existingCategory);
        logger.info("Updated category with id: {}", existingCategory.getId());
        
        return convertToDto(existingCategory);
    }

    public void deleteCategory(Long id) {
        logger.info("Deleting category with id: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        
        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with existing products. Remove products first.");
        }
        
        category.setActive(false);
        categoryRepository.save(category);
        logger.info("Soft deleted category with id: {}", id);
    }

    @Transactional(readOnly = true)
    public long getActiveCount() {
        return categoryRepository.countActiveCategories();
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setActive(category.getActive());
        dto.setProductCount((long) category.getProducts().size());
        return dto;
    }

    private Category convertToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setActive(dto.getActive() != null ? dto.getActive() : true);
        return category;
    }
}