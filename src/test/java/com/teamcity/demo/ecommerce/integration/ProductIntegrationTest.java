package com.teamcity.demo.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcity.demo.ecommerce.dto.ProductDto;
import com.teamcity.demo.ecommerce.entity.Category;
import com.teamcity.demo.ecommerce.entity.Product;
import com.teamcity.demo.ecommerce.repository.CategoryRepository;
import com.teamcity.demo.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvctest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "logging.level.org.springframework.web=DEBUG"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Product Integration Tests")
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    @Transactional
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        testCategory = new Category("Electronics", "Electronic devices and gadgets");
        testCategory = categoryRepository.save(testCategory);

        testProduct = new Product("Smartphone", "PHONE-001", new BigDecimal("699.99"));
        testProduct.setDescription("Latest smartphone with great features");
        testProduct.setQuantity(50);
        testProduct.setActive(true);
        testProduct.setBrand("TechCorp");
        testProduct.setCategory(testCategory);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    @DisplayName("Should get all products successfully")
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].sku").value("PHONE-001"))
                .andExpect(jsonPath("$.content[0].name").value("Smartphone"))
                .andExpect(jsonPath("$.content[0].categoryName").value("Electronics"));
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProduct() throws Exception {
        ProductDto newProduct = new ProductDto();
        newProduct.setName("Laptop");
        newProduct.setSku("LAPTOP-001");
        newProduct.setDescription("High-performance laptop");
        newProduct.setPrice(new BigDecimal("1299.99"));
        newProduct.setQuantity(20);
        newProduct.setActive(true);
        newProduct.setBrand("CompCorp");
        newProduct.setCategoryId(testCategory.getId());

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("LAPTOP-001"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProduct() throws Exception {
        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Smartphone");
        updateDto.setSku("PHONE-001");
        updateDto.setDescription("Updated smartphone description");
        updateDto.setPrice(new BigDecimal("749.99"));
        updateDto.setQuantity(75);
        updateDto.setActive(true);
        updateDto.setBrand("TechCorp");
        updateDto.setCategoryId(testCategory.getId());

        mockMvc.perform(put("/v1/products/" + testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Smartphone"))
                .andExpect(jsonPath("$.price").value(749.99))
                .andExpect(jsonPath("$.quantity").value(75));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/v1/products/" + testProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("Should search products successfully")
    void shouldSearchProducts() throws Exception {
        mockMvc.perform(get("/v1/products/search?q=smartphone"))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Smartphone"));

        mockMvc.perform(get("/v1/products/search?q=notfound"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("Should get products by category successfully")
    void shouldGetProductsByCategory() throws Exception {
        mockMvc.perform(get("/v1/products/category/" + testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpected(jsonPath("$.content[0].categoryId").value(testCategory.getId()));
    }

    @Test
    @DisplayName("Should get products by price range successfully")
    void shouldGetProductsByPriceRange() throws Exception {
        mockMvc.perform(get("/v1/products/price-range?minPrice=600&maxPrice=800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].price").value(699.99));

        mockMvc.perform(get("/v1/products/price-range?minPrice=1000&maxPrice=2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("Should update stock successfully")
    void shouldUpdateStock() throws Exception {
        mockMvc.perform(patch("/v1/products/" + testProduct.getId() + "/stock?quantity=100"))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    @DisplayName("Should get low stock products successfully")
    void shouldGetLowStockProducts() throws Exception {
        // Set product to low stock
        testProduct.setQuantity(5);
        productRepository.save(testProduct);

        mockMvc.perform(get("/v1/products/low-stock?threshold=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    @DisplayName("Should return validation errors for invalid input")
    void shouldReturnValidationErrors() throws Exception {
        ProductDto invalidProduct = new ProductDto();
        // Missing required fields

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    @DisplayName("Should return 404 for non-existent product")
    void shouldReturn404ForNonExistentProduct() throws Exception {
        mockMvc.perform(get("/v1/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ENTITY_NOT_FOUND"));
    }

    @Test
    @DisplayName("Should return 400 for duplicate SKU")
    void shouldReturn400ForDuplicateSku() throws Exception {
        ProductDto duplicateProduct = new ProductDto();
        duplicateProduct.setName("Duplicate Phone");
        duplicateProduct.setSku("PHONE-001"); // Same SKU as existing product
        duplicateProduct.setPrice(new BigDecimal("599.99"));
        duplicateProduct.setCategoryId(testCategory.getId());

        mockMvc.perform(post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_ARGUMENT"));
    }
}