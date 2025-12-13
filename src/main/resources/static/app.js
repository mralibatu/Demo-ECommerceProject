const API_BASE_URL = '/api/products';

// DOM elements
const productsTable = document.getElementById('productsTable');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const refreshBtn = document.getElementById('refreshBtn');
const productForm = document.getElementById('productForm');
const loadingSpinner = document.getElementById('loading');
const totalProductsEl = document.getElementById('totalProducts');
const totalValueEl = document.getElementById('totalValue');

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    loadProducts();
    loadStats();
    setupEventListeners();
});

// Event listeners
function setupEventListeners() {
    refreshBtn.addEventListener('click', () => {
        loadProducts();
        loadStats();
    });
    
    searchBtn.addEventListener('click', handleSearch);
    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });
    
    productForm.addEventListener('submit', handleAddProduct);
}

// Load all products
async function loadProducts() {
    try {
        showLoading(true);
        const response = await fetch(API_BASE_URL);
        const products = await response.json();
        displayProducts(products);
        showNotification('Products loaded successfully', 'success');
    } catch (error) {
        console.error('Error loading products:', error);
        showNotification('Failed to load products', 'error');
    } finally {
        showLoading(false);
    }
}

// Load statistics
async function loadStats() {
    try {
        const response = await fetch(`${API_BASE_URL}/stats`);
        const stats = await response.json();
        totalProductsEl.textContent = stats.totalProducts;
        totalValueEl.textContent = `$${stats.totalValue.toFixed(2)}`;
    } catch (error) {
        console.error('Error loading stats:', error);
        totalProductsEl.textContent = 'Error';
        totalValueEl.textContent = 'Error';
    }
}

// Search products
async function handleSearch() {
    const searchTerm = searchInput.value.trim();
    if (!searchTerm) {
        loadProducts();
        return;
    }
    
    try {
        showLoading(true);
        const response = await fetch(`${API_BASE_URL}/search?name=${encodeURIComponent(searchTerm)}`);
        const products = await response.json();
        displayProducts(products);
        showNotification(`Found ${products.length} product(s)`, 'info');
    } catch (error) {
        console.error('Error searching products:', error);
        showNotification('Search failed', 'error');
    } finally {
        showLoading(false);
    }
}

// Add new product
async function handleAddProduct(e) {
    e.preventDefault();
    
    const product = {
        name: document.getElementById('productName').value,
        description: document.getElementById('productDesc').value,
        price: parseFloat(document.getElementById('productPrice').value),
        quantity: parseInt(document.getElementById('productQuantity').value)
    };
    
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(product)
        });
        
        if (response.ok) {
            productForm.reset();
            loadProducts();
            loadStats();
            showNotification('Product added successfully', 'success');
        } else {
            throw new Error('Failed to add product');
        }
    } catch (error) {
        console.error('Error adding product:', error);
        showNotification('Failed to add product', 'error');
    }
}

// Delete product
async function deleteProduct(id) {
    if (!confirm('Are you sure you want to delete this product?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            loadProducts();
            loadStats();
            showNotification('Product deleted successfully', 'success');
        } else {
            throw new Error('Failed to delete product');
        }
    } catch (error) {
        console.error('Error deleting product:', error);
        showNotification('Failed to delete product', 'error');
    }
}

// Display products in table
function displayProducts(products) {
    if (products.length === 0) {
        productsTable.innerHTML = '<tr><td colspan="6" class="text-center">No products found</td></tr>';
        return;
    }
    
    productsTable.innerHTML = products.map(product => `
        <tr>
            <td>${product.id}</td>
            <td><strong>${product.name}</strong></td>
            <td>${product.description || '-'}</td>
            <td class="text-success"><strong>$${product.price.toFixed(2)}</strong></td>
            <td>
                <span class="badge ${product.quantity < 10 ? 'bg-warning' : 'bg-success'}">
                    ${product.quantity} units
                </span>
            </td>
            <td>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteProduct(${product.id})">
                    🗑️ Delete
                </button>
            </td>
        </tr>
    `).join('');
}

// Show/hide loading spinner
function showLoading(show) {
    loadingSpinner.style.display = show ? 'block' : 'none';
}

// Show notification toast
function showNotification(message, type = 'info') {
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');
    
    // Set message and style
    toastMessage.textContent = message;
    toast.className = `toast position-fixed bottom-0 end-0 m-3 border-${getBootstrapColor(type)}`;
    
    // Show toast
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
}

// Get Bootstrap color class
function getBootstrapColor(type) {
    switch (type) {
        case 'success': return 'success';
        case 'error': return 'danger';
        case 'warning': return 'warning';
        case 'info':
        default: return 'info';
    }
}