package com.teamcity.demo.ecommerce.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:e2edb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DisplayName("Product E2E Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeAll
    void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode for CI/CD
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void setUp() {
        driver.get(baseUrl);
    }

    @Test
    @DisplayName("Should load product management page successfully")
    void shouldLoadProductManagementPage() {
        driver.get(baseUrl + "/products");
        
        WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.tagName("h1")));
        assertEquals("Product Management", title.getText());
    }

    @Test
    @DisplayName("Should display products list")
    void shouldDisplayProductsList() {
        driver.get(baseUrl + "/products");
        
        // Wait for products table to load
        WebElement productsTable = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.id("products-table")));
        assertNotNull(productsTable);
        
        // Check if sample products are displayed
        List<WebElement> productRows = driver.findElements(By.cssSelector("#products-table tbody tr"));
        assertTrue(productRows.size() > 0, "Should display at least one product");
    }

    @Test
    @DisplayName("Should create new product successfully")
    void shouldCreateNewProduct() {
        driver.get(baseUrl + "/products");
        
        // Click Add Product button
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.id("add-product-btn")));
        addButton.click();
        
        // Fill product form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-form")));
        
        driver.findElement(By.id("product-name")).sendKeys("E2E Test Product");
        driver.findElement(By.id("product-sku")).sendKeys("E2E-001");
        driver.findElement(By.id("product-description")).sendKeys("Product created via E2E test");
        driver.findElement(By.id("product-price")).sendKeys("199.99");
        driver.findElement(By.id("product-quantity")).sendKeys("25");
        
        // Select category
        driver.findElement(By.id("product-category")).sendKeys("Electronics");
        
        // Submit form
        WebElement submitButton = driver.findElement(By.id("submit-product"));
        submitButton.click();
        
        // Verify success message
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".alert-success")));
        assertTrue(successMessage.getText().contains("Product created successfully"));
        
        // Verify product appears in list
        WebElement productCell = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[text()='E2E Test Product']")));
        assertNotNull(productCell);
    }

    @Test
    @DisplayName("Should search products successfully")
    void shouldSearchProducts() {
        driver.get(baseUrl + "/products");
        
        // Use search functionality
        WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.id("search-input")));
        searchInput.sendKeys("Smartphone");
        
        WebElement searchButton = driver.findElement(By.id("search-btn"));
        searchButton.click();
        
        // Wait for search results
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("products-table")));
        
        // Verify search results
        List<WebElement> searchResults = driver.findElements(By.cssSelector("#products-table tbody tr"));
        assertTrue(searchResults.size() > 0, "Should find products matching 'Smartphone'");
        
        // Verify that results contain the search term
        boolean foundMatchingProduct = false;
        for (WebElement row : searchResults) {
            if (row.getText().toLowerCase().contains("smartphone")) {
                foundMatchingProduct = true;
                break;
            }
        }
        assertTrue(foundMatchingProduct, "Search results should contain products matching the search term");
    }

    @Test
    @DisplayName("Should edit product successfully")
    void shouldEditProduct() {
        driver.get(baseUrl + "/products");
        
        // Click edit button for first product
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".edit-product-btn:first-of-type")));
        editButton.click();
        
        // Wait for edit form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit-product-form")));
        
        // Update product name
        WebElement nameField = driver.findElement(By.id("edit-product-name"));
        nameField.clear();
        nameField.sendKeys("Updated Product Name");
        
        // Update price
        WebElement priceField = driver.findElement(By.id("edit-product-price"));
        priceField.clear();
        priceField.sendKeys("299.99");
        
        // Submit changes
        WebElement updateButton = driver.findElement(By.id("update-product"));
        updateButton.click();
        
        // Verify success message
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".alert-success")));
        assertTrue(successMessage.getText().contains("Product updated successfully"));
    }

    @Test
    @DisplayName("Should filter products by category")
    void shouldFilterProductsByCategory() {
        driver.get(baseUrl + "/products");
        
        // Select category filter
        WebElement categoryFilter = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.id("category-filter")));
        categoryFilter.sendKeys("Electronics");
        
        // Apply filter
        WebElement filterButton = driver.findElement(By.id("apply-filter"));
        filterButton.click();
        
        // Wait for filtered results
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("products-table")));
        
        // Verify all displayed products are in Electronics category
        List<WebElement> categoryCards = driver.findElements(By.cssSelector(".product-category"));
        for (WebElement categoryCard : categoryCards) {
            assertEquals("Electronics", categoryCard.getText());
        }
    }

    @Test
    @DisplayName("Should display product details")
    void shouldDisplayProductDetails() {
        driver.get(baseUrl + "/products");
        
        // Click on first product to view details
        WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".product-name-link:first-of-type")));
        String productName = productLink.getText();
        productLink.click();
        
        // Verify product details page
        WebElement detailsTitle = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.id("product-details-title")));
        assertEquals(productName, detailsTitle.getText());
        
        // Verify product information is displayed
        assertNotNull(driver.findElement(By.id("product-sku")));
        assertNotNull(driver.findElement(By.id("product-price")));
        assertNotNull(driver.findElement(By.id("product-description")));
        assertNotNull(driver.findElement(By.id("product-stock")));
    }

    @Test
    @DisplayName("Should handle form validation errors")
    void shouldHandleFormValidationErrors() {
        driver.get(baseUrl + "/products");
        
        // Click Add Product button
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.id("add-product-btn")));
        addButton.click();
        
        // Try to submit empty form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-form")));
        WebElement submitButton = driver.findElement(By.id("submit-product"));
        submitButton.click();
        
        // Verify validation errors are displayed
        List<WebElement> errorMessages = driver.findElements(By.cssSelector(".field-error"));
        assertTrue(errorMessages.size() > 0, "Should display validation errors for required fields");
        
        // Verify specific error messages
        boolean hasNameError = false;
        boolean hasSkuError = false;
        boolean hasPriceError = false;
        
        for (WebElement error : errorMessages) {
            String errorText = error.getText().toLowerCase();
            if (errorText.contains("name") && errorText.contains("required")) {
                hasNameError = true;
            }
            if (errorText.contains("sku") && errorText.contains("required")) {
                hasSkuError = true;
            }
            if (errorText.contains("price") && errorText.contains("required")) {
                hasPriceError = true;
            }
        }
        
        assertTrue(hasNameError, "Should display error for missing product name");
        assertTrue(hasSkuError, "Should display error for missing SKU");
        assertTrue(hasPriceError, "Should display error for missing price");
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProduct() {
        driver.get(baseUrl + "/products");
        
        // Count initial products
        List<WebElement> initialProducts = driver.findElements(By.cssSelector("#products-table tbody tr"));
        int initialCount = initialProducts.size();
        
        // Click delete button for first product
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".delete-product-btn:first-of-type")));
        deleteButton.click();
        
        // Confirm deletion in modal
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.id("confirm-delete")));
        confirmButton.click();
        
        // Verify success message
        WebElement successMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector(".alert-success")));
        assertTrue(successMessage.getText().contains("Product deleted successfully"));
        
        // Verify product count decreased
        List<WebElement> updatedProducts = driver.findElements(By.cssSelector("#products-table tbody tr"));
        assertEquals(initialCount - 1, updatedProducts.size(), "Product count should decrease by 1");
    }
}