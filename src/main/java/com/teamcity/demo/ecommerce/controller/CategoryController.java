package com.teamcity.demo.ecommerce.controller;

import com.teamcity.demo.ecommerce.dto.CategoryDto;
import com.teamcity.demo.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Categories", description = "Category management operations")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve a paginated list of all active categories")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<CategoryDto>> getAllCategories(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("GET /v1/categories - Fetching categories with pagination: {}", pageable);
        Page<CategoryDto> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/list")
    @Operation(summary = "Get all categories as list", description = "Retrieve all active categories as a simple list")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<List<CategoryDto>> getAllCategoriesAsList() {
        logger.info("GET /v1/categories/list - Fetching all categories as list");
        List<CategoryDto> categories = categoryService.getAllCategoriesAsList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable @Parameter(description = "Category ID") Long id) {
        logger.info("GET /v1/categories/{} - Fetching category", id);
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get category by name", description = "Retrieve a specific category by its name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryDto> getCategoryByName(
            @PathVariable @Parameter(description = "Category name") String name) {
        logger.info("GET /v1/categories/name/{} - Fetching category", name);
        CategoryDto category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search")
    @Operation(summary = "Search categories", description = "Search categories by name or description")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<Page<CategoryDto>> searchCategories(
            @RequestParam @Parameter(description = "Search term") String q,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("GET /v1/categories/search?q={} - Searching categories", q);
        Page<CategoryDto> categories = categoryService.searchCategories(q, pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/with-products")
    @Operation(summary = "Get categories with products", description = "Retrieve categories that have at least one product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<List<CategoryDto>> getCategoriesWithProducts() {
        logger.info("GET /v1/categories/with-products - Fetching categories with products");
        List<CategoryDto> categories = categoryService.getCategoriesWithProducts();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Create a new category")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category data"),
        @ApiResponse(responseCode = "409", description = "Category with same name already exists")
    })
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logger.info("POST /v1/categories - Creating category with name: {}", categoryDto.getName());
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category data"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category with same name already exists")
    })
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable @Parameter(description = "Category ID") Long id,
            @Valid @RequestBody CategoryDto categoryDto) {
        logger.info("PUT /v1/categories/{} - Updating category", id);
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Soft delete a category (mark as inactive)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete category with existing products")
    })
    public ResponseEntity<Void> deleteCategory(
            @PathVariable @Parameter(description = "Category ID") Long id) {
        logger.info("DELETE /v1/categories/{} - Deleting category", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}