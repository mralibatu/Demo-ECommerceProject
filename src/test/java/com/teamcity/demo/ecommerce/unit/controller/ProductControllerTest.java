package com.teamcity.demo.ecommerce.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcity.demo.ecommerce.controller.ProductController;
import com.teamcity.demo.ecommerce.dto.ProductDto;
import com.teamcity.demo.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("Product Controller Unit Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto testProductDto;

    @BeforeEach
    void setUp() {
        testProductDto = new ProductDto();
        testProductDto.setId(1L);
        testProductDto.setName("Test Product");
        testProductDto.setSku("TEST-001");
        testProductDto.setPrice(new BigDecimal("99.99"));
        testProductDto.setDescription("Test product description");
        testProductDto.setQuantity(10);
        testProductDto.setActive(true);
        testProductDto.setCategoryId(1L);
        testProductDto.setCategoryName("Electronics");
    }

    @Test
    @DisplayName("Should get all products successfully")
    void shouldGetAllProducts() throws Exception {
        List<ProductDto> products = Arrays.asList(testProductDto);
        Page<ProductDto> productPage = new PageImpl<>(products);

        when(productService.getAllProducts(any())).thenReturn(productPage);

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sku").value("TEST-001"))
                .andExpect(jsonPath("$.content[0].name").value("Test Product"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should get product by ID successfully")
    void shouldGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(testProductDto);

        mockMvc.perform(get("/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("TEST-001"))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/v1/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ENTITY_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProduct() throws Exception {
        ProductDto createDto = new ProductDto();
        createDto.setName("New Product");
        createDto.setSku("NEW-001");
        createDto.setPrice(new BigDecimal("149.99"));
        createDto.setCategoryId(1L);

        when(productService.createProduct(any(ProductDto.class))).thenReturn(testProductDto);

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("TEST-001"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("Should return 400 for invalid product data")
    void shouldReturn400ForInvalidProductData() throws Exception {
        ProductDto invalidDto = new ProductDto();
        // Missing required fields like name, sku, price

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(testProductDto);

        mockMvc.perform(put("/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("TEST-001"))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should search products successfully")
    void shouldSearchProducts() throws Exception {
        List<ProductDto> products = Arrays.asList(testProductDto);
        Page<ProductDto> productPage = new PageImpl<>(products);

        when(productService.searchProducts(eq("test"), any())).thenReturn(productPage);

        mockMvc.perform(get("/v1/products/search?q=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sku").value("TEST-001"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should get products by category successfully")
    void shouldGetProductsByCategory() throws Exception {
        List<ProductDto> products = Arrays.asList(testProductDto);
        Page<ProductDto> productPage = new PageImpl<>(products);

        when(productService.getProductsByCategory(eq(1L), any())).thenReturn(productPage);

        mockMvc.perform(get("/v1/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryId").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should get products by price range successfully")
    void shouldGetProductsByPriceRange() throws Exception {
        List<ProductDto> products = Arrays.asList(testProductDto);
        Page<ProductDto> productPage = new PageImpl<>(products);

        when(productService.getProductsByPriceRange(any(BigDecimal.class), any(BigDecimal.class), any()))
                .thenReturn(productPage);

        mockMvc.perform(get("/v1/products/price-range?minPrice=50&maxPrice=150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(99.99))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() throws Exception {
        testProductDto.setQuantity(25);
        when(productService.updateStock(1L, 25)).thenReturn(testProductDto);

        mockMvc.perform(patch("/v1/products/1/stock?quantity=25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(25));
    }

    @Test
    @DisplayName("Should get low stock products successfully")
    void shouldGetLowStockProducts() throws Exception {
        testProductDto.setQuantity(5);
        List<ProductDto> lowStockProducts = Arrays.asList(testProductDto);

        when(productService.getLowStockProducts(10)).thenReturn(lowStockProducts);

        mockMvc.perform(get("/v1/products/low-stock?threshold=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(5))
                .andExpect(jsonPath("$.length()").value(1));
    }
}