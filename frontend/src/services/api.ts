import axios, { AxiosError, AxiosResponse } from 'axios';
import { Product, Category, PagedResponse, ApiError } from '../types/Product';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    console.log(`Making ${config.method?.toUpperCase()} request to ${config.url}`);
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error: AxiosError<ApiError>) => {
    console.error('Response error:', error.response?.data);
    return Promise.reject(error);
  }
);

export const productApi = {
  // Get all products with pagination
  getProducts: async (page = 0, size = 20, sort = 'name'): Promise<PagedResponse<Product>> => {
    const response = await api.get(`/v1/products?page=${page}&size=${size}&sort=${sort}`);
    return response.data;
  },

  // Get product by ID
  getProduct: async (id: number): Promise<Product> => {
    const response = await api.get(`/v1/products/${id}`);
    return response.data;
  },

  // Get product by SKU
  getProductBySku: async (sku: string): Promise<Product> => {
    const response = await api.get(`/v1/products/sku/${sku}`);
    return response.data;
  },

  // Search products
  searchProducts: async (
    query: string,
    page = 0,
    size = 20,
    sort = 'name'
  ): Promise<PagedResponse<Product>> => {
    const response = await api.get(
      `/v1/products/search?q=${encodeURIComponent(query)}&page=${page}&size=${size}&sort=${sort}`
    );
    return response.data;
  },

  // Get products by category
  getProductsByCategory: async (
    categoryId: number,
    page = 0,
    size = 20,
    sort = 'name'
  ): Promise<PagedResponse<Product>> => {
    const response = await api.get(
      `/v1/products/category/${categoryId}?page=${page}&size=${size}&sort=${sort}`
    );
    return response.data;
  },

  // Get products by price range
  getProductsByPriceRange: async (
    minPrice: number,
    maxPrice: number,
    page = 0,
    size = 20,
    sort = 'price'
  ): Promise<PagedResponse<Product>> => {
    const response = await api.get(
      `/v1/products/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}&page=${page}&size=${size}&sort=${sort}`
    );
    return response.data;
  },

  // Get low stock products
  getLowStockProducts: async (threshold = 10): Promise<Product[]> => {
    const response = await api.get(`/v1/products/low-stock?threshold=${threshold}`);
    return response.data;
  },

  // Create product
  createProduct: async (product: Omit<Product, 'id'>): Promise<Product> => {
    const response = await api.post('/v1/products', product);
    return response.data;
  },

  // Update product
  updateProduct: async (id: number, product: Partial<Product>): Promise<Product> => {
    const response = await api.put(`/v1/products/${id}`, product);
    return response.data;
  },

  // Update product stock
  updateStock: async (id: number, quantity: number): Promise<Product> => {
    const response = await api.patch(`/v1/products/${id}/stock?quantity=${quantity}`);
    return response.data;
  },

  // Delete product (soft delete)
  deleteProduct: async (id: number): Promise<void> => {
    await api.delete(`/v1/products/${id}`);
  },
};

export const categoryApi = {
  // Get all categories with pagination
  getCategories: async (page = 0, size = 20, sort = 'name'): Promise<PagedResponse<Category>> => {
    const response = await api.get(`/v1/categories?page=${page}&size=${size}&sort=${sort}`);
    return response.data;
  },

  // Get all categories as list
  getCategoriesList: async (): Promise<Category[]> => {
    const response = await api.get('/v1/categories/list');
    return response.data;
  },

  // Get category by ID
  getCategory: async (id: number): Promise<Category> => {
    const response = await api.get(`/v1/categories/${id}`);
    return response.data;
  },

  // Search categories
  searchCategories: async (
    query: string,
    page = 0,
    size = 20,
    sort = 'name'
  ): Promise<PagedResponse<Category>> => {
    const response = await api.get(
      `/v1/categories/search?q=${encodeURIComponent(query)}&page=${page}&size=${size}&sort=${sort}`
    );
    return response.data;
  },

  // Get categories with products
  getCategoriesWithProducts: async (): Promise<Category[]> => {
    const response = await api.get('/v1/categories/with-products');
    return response.data;
  },

  // Create category
  createCategory: async (category: Omit<Category, 'id' | 'productCount'>): Promise<Category> => {
    const response = await api.post('/v1/categories', category);
    return response.data;
  },

  // Update category
  updateCategory: async (id: number, category: Partial<Category>): Promise<Category> => {
    const response = await api.put(`/v1/categories/${id}`, category);
    return response.data;
  },

  // Delete category
  deleteCategory: async (id: number): Promise<void> => {
    await api.delete(`/v1/categories/${id}`);
  },
};

export default api;