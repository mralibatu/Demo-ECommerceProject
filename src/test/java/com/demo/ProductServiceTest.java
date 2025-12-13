package com.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Test Product", "Test Description", 99.99, 10);
        testProduct.setId(1L);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.getAllProducts();

        // Then
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testProduct, result.get());
    }

    @Test
    void getProductById_ShouldReturnEmpty_WhenProductDoesNotExist() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void saveProduct_ShouldReturnSavedProduct() {
        // Given
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        // When
        Product savedProduct = productService.saveProduct(testProduct);

        // Then
        assertEquals(testProduct, savedProduct);
        verify(productRepository).save(testProduct);
    }

    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).deleteById(1L);
    }

    @Test
    void calculateTotalValue_ShouldReturnCorrectTotal() {
        // Given
        Product product1 = new Product("Product 1", "Desc 1", 10.0, 5);
        Product product2 = new Product("Product 2", "Desc 2", 20.0, 3);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        // When
        double totalValue = productService.calculateTotalValue();

        // Then
        assertEquals(110.0, totalValue); // (10*5) + (20*3) = 50 + 60 = 110
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        // Given
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findByNameContaining("Test")).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.searchProducts("Test");

        // Then
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository).findByNameContaining("Test");
    }

    @Test
    void getLowStockProducts_ShouldReturnLowStockProducts() {
        // Given
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findLowStockProducts(5)).thenReturn(expectedProducts);

        // When
        List<Product> actualProducts = productService.getLowStockProducts(5);

        // Then
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository).findLowStockProducts(5);
    }
}