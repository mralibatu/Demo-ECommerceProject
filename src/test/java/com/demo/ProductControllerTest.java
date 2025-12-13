package com.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Test Product", "Test Description", 99.99, 10);
        testProduct.setId(1L);
    }

    @Test
    void getAllProducts_ShouldReturnProductList() throws Exception {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value(99.99));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_ShouldReturnNotFound_WhenProductDoesNotExist() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        when(productService.saveProduct(any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() throws Exception {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productService.searchProducts("Test")).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products/search")
                .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void getStats_ShouldReturnProductStats() throws Exception {
        // Given
        when(productService.getProductCount()).thenReturn(5L);
        when(productService.calculateTotalValue()).thenReturn(1000.0);

        // When & Then
        mockMvc.perform(get("/api/products/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProducts").value(5))
                .andExpect(jsonPath("$.totalValue").value(1000.0));
    }
}