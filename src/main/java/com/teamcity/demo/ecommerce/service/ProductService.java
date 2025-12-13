package com.teamcity.demo.ecommerce.service;

import com.teamcity.demo.ecommerce.dto.ProductDto;
import com.teamcity.demo.ecommerce.entity.Category;
import com.teamcity.demo.ecommerce.entity.Product;
import com.teamcity.demo.ecommerce.repository.CategoryRepository;
import com.teamcity.demo.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        logger.debug("Fetching all active products with pagination: {}", pageable);
        return productRepository.findByActiveTrue(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        logger.debug("Fetching product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return convertToDto(product);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductBySku(String sku) {
        logger.debug("Fetching product by SKU: {}", sku);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with SKU: " + sku));
        return convertToDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> searchProducts(String searchTerm, Pageable pageable) {
        logger.debug("Searching products with term: {} and pagination: {}", searchTerm, pageable);
        return productRepository.findBySearchTerm(searchTerm, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        logger.debug("Fetching products by category id: {} with pagination: {}", categoryId, pageable);
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.debug("Fetching products in price range: {} - {} with pagination: {}", minPrice, maxPrice, pageable);
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getLowStockProducts(Integer threshold) {
        logger.debug("Fetching low stock products with threshold: {}", threshold);
        return productRepository.findLowStockProducts(threshold)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductDto productDto) {
        logger.info("Creating new product with SKU: {}", productDto.getSku());
        
        if (productRepository.existsBySku(productDto.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + productDto.getSku() + " already exists");
        }

        Product product = convertToEntity(productDto);
        product = productRepository.save(product);
        logger.info("Created product with id: {}", product.getId());
        
        return convertToDto(product);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        logger.info("Updating product with id: {}", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (productRepository.existsBySkuAndIdNot(productDto.getSku(), id)) {
            throw new IllegalArgumentException("Product with SKU " + productDto.getSku() + " already exists");
        }

        updateProductFromDto(existingProduct, productDto);
        existingProduct = productRepository.save(existingProduct);
        logger.info("Updated product with id: {}", existingProduct.getId());
        
        return convertToDto(existingProduct);
    }

    public void deleteProduct(Long id) {
        logger.info("Deleting product with id: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        product.setActive(false);
        productRepository.save(product);
        logger.info("Soft deleted product with id: {}", id);
    }

    public ProductDto updateStock(Long id, Integer newQuantity) {
        logger.info("Updating stock for product id: {} to quantity: {}", id, newQuantity);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        product.setQuantity(newQuantity);
        product = productRepository.save(product);
        logger.info("Updated stock for product id: {} to quantity: {}", id, newQuantity);
        
        return convertToDto(product);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setActive(product.getActive());
        dto.setImageUrl(product.getImageUrl());
        dto.setWeight(product.getWeight());
        dto.setBrand(product.getBrand());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        return dto;
    }

    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        updateProductFromDto(product, dto);
        return product;
    }

    private void updateProductFromDto(Product product, ProductDto dto) {
        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        product.setActive(dto.getActive() != null ? dto.getActive() : true);
        product.setImageUrl(dto.getImageUrl());
        product.setWeight(dto.getWeight());
        product.setBrand(dto.getBrand());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }
    }
}