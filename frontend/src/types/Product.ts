export interface Product {
  id?: number;
  name: string;
  sku: string;
  description?: string;
  price: number;
  quantity: number;
  active: boolean;
  imageUrl?: string;
  weight?: number;
  brand?: string;
  categoryId?: number;
  categoryName?: string;
}

export interface Category {
  id?: number;
  name: string;
  description?: string;
  active: boolean;
  productCount?: number;
}

export interface ProductFilters {
  search?: string;
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  inStock?: boolean;
}

export interface PagedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ApiError {
  code: string;
  message: string;
  timestamp: string;
  fieldErrors?: { [key: string]: string };
}