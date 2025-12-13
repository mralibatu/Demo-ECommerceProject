package com.teamcity.demo.ecommerce.unit.service;

import com.teamcity.demo.ecommerce.dto.ProductDto;
import com.teamcity.demo.ecommerce.entity.Category;
import com.teamcity.demo.ecommerce.entity.Product;
import com.teamcity.demo.ecommerce.repository.CategoryRepository;
import com.teamcity.demo.ecommerce.repository.ProductRepository;
import com.teamcity.demo.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;
    private ProductDto testProductDto;

    @BeforeEach
    void setUp() {
        testCategory = new Category("Electronics", "Electronic devices");
        testCategory.setId(1L);

        testProduct = new Product("Test Product", "TEST-001", new BigDecimal("99.99"));
        testProduct.setId(1L);
        testProduct.setDescription("Test product description");
        testProduct.setQuantity(10);
        testProduct.setActive(true);
        testProduct.setCategory(testCategory);

        testProductDto = new ProductDto();
        testProductDto.setName("Test Product");
        testProductDto.setSku("TEST-001");
        testProductDto.setPrice(new BigDecimal("99.99"));
        testProductDto.setDescription("Test product description");
        testProductDto.setQuantity(10);
        testProductDto.setActive(true);
        testProductDto.setCategoryId(1L);
    }

    @Test
    @DisplayName("Should get all products successfully")
    void shouldGetAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 20);

        when(productRepository.findByActiveTrue(pageable)).thenReturn(productPage);

        Page<ProductDto> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("TEST-001", result.getContent().get(0).getSku());
        verify(productRepository).findByActiveTrue(pageable);
    }

    @Test
    @DisplayName("Should get product by ID successfully")
    void shouldGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("TEST-001", result.getSku());
        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when product not found by ID")
    void shouldThrowExceptionWhenProductNotFoundById() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get product by SKU successfully")
    void shouldGetProductBySku() {
        when(productRepository.findBySku("TEST-001")).thenReturn(Optional.of(testProduct));

        ProductDto result = productService.getProductBySku("TEST-001");

        assertNotNull(result);
        assertEquals("TEST-001", result.getSku());
        assertEquals("Test Product", result.getName());
        verify(productRepository).findBySku("TEST-001");
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProduct() {
        when(productRepository.existsBySku("TEST-001")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDto result = productService.createProduct(testProductDto);

        assertNotNull(result);
        assertEquals("TEST-001", result.getSku());
        assertEquals("Test Product", result.getName());
        verify(productRepository).existsBySku("TEST-001");
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw exception when creating product with existing SKU")
    void shouldThrowExceptionWhenCreatingProductWithExistingSku() {
        when(productRepository.existsBySku("TEST-001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(testProductDto));
        verify(productRepository).existsBySku("TEST-001");
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProduct() {
        Product existingProduct = new Product("Old Product", "TEST-001", new BigDecimal("89.99"));
        existingProduct.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.existsBySkuAndIdNot("TEST-001", 1L)).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDto result = productService.updateProduct(1L, testProductDto);

        assertNotNull(result);
        assertEquals("TEST-001", result.getSku());
        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).existsBySkuAndIdNot("TEST-001", 1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product successfully (soft delete)")
    void shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDto result = productService.updateStock(1L, 20);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should search products successfully")
    void shouldSearchProducts() {
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 20);

        when(productRepository.findBySearchTerm("test", pageable)).thenReturn(productPage);

        Page<ProductDto> result = productService.searchProducts("test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findBySearchTerm("test", pageable);
    }

    @Test
    @DisplayName("Should get products by price range successfully")
    void shouldGetProductsByPriceRange() {
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 20);
        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("150.00");

        when(productRepository.findByPriceBetween(minPrice, maxPrice, pageable)).thenReturn(productPage);

        Page<ProductDto> result = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByPriceBetween(minPrice, maxPrice, pageable);
    }

    @Test
    @DisplayName("Should get low stock products successfully")
    void shouldGetLowStockProducts() {
        testProduct.setQuantity(5);
        List<Product> products = Arrays.asList(testProduct);

        when(productRepository.findLowStockProducts(10)).thenReturn(products);

        List<ProductDto> result = productService.getLowStockProducts(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getQuantity());
        verify(productRepository).findLowStockProducts(10);
    }
}