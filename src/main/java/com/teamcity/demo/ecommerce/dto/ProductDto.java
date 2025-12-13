package com.teamcity.demo.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Product Data Transfer Object")
public class ProductDto {

    @Schema(description = "Product unique identifier", example = "1")
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    @Schema(description = "Product name", example = "Smartphone Pro Max", required = true)
    private String name;

    @NotBlank(message = "SKU is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU must contain only uppercase letters, numbers and hyphens")
    @Schema(description = "Stock Keeping Unit", example = "PHONE-001", required = true)
    private String sku;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Product description", example = "Latest flagship smartphone with advanced camera system")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "999.99", required = true)
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Schema(description = "Available quantity", example = "50")
    private Integer quantity;

    @Schema(description = "Product active status", example = "true")
    private Boolean active;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Schema(description = "Product image URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    @Schema(description = "Product weight in kg", example = "0.200")
    private BigDecimal weight;

    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    @Schema(description = "Product brand", example = "TechCorp")
    private String brand;

    @Schema(description = "Category ID", example = "1")
    private Long categoryId;

    @Schema(description = "Category name", example = "Electronics")
    private String categoryName;

    public ProductDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}