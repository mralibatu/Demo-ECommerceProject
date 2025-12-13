import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Table, Form, InputGroup, Alert, Spinner, Pagination } from 'react-bootstrap';
import { Product, Category, PagedResponse } from '../types/Product';
import { productApi, categoryApi } from '../services/api';
import { toast } from 'react-toastify';

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<PagedResponse<Product>>({
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 20,
    number: 0,
    first: true,
    last: true,
  });
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<number | ''>('');
  const [currentPage, setCurrentPage] = useState(0);
  const [sortBy, setSortBy] = useState('name');

  // Load products
  const loadProducts = async (page = 0, search = '', categoryId: number | '' = '') => {
    try {
      setLoading(true);
      setError(null);

      let response: PagedResponse<Product>;

      if (search.trim()) {
        response = await productApi.searchProducts(search, page, 20, sortBy);
      } else if (categoryId) {
        response = await productApi.getProductsByCategory(Number(categoryId), page, 20, sortBy);
      } else {
        response = await productApi.getProducts(page, 20, sortBy);
      }

      setProducts(response);
      setCurrentPage(page);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Failed to load products';
      setError(errorMessage);
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  // Load categories
  const loadCategories = async () => {
    try {
      const categoriesList = await categoryApi.getCategoriesList();
      setCategories(categoriesList);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Failed to load categories';
      toast.error(errorMessage);
    }
  };

  // Handle search
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setSelectedCategory('');
    loadProducts(0, searchTerm, '');
  };

  // Handle category filter
  const handleCategoryChange = (categoryId: number | '') => {
    setSelectedCategory(categoryId);
    setSearchTerm('');
    loadProducts(0, '', categoryId);
  };

  // Handle sort change
  const handleSortChange = (newSort: string) => {
    setSortBy(newSort);
    loadProducts(currentPage, searchTerm, selectedCategory);
  };

  // Handle page change
  const handlePageChange = (page: number) => {
    loadProducts(page, searchTerm, selectedCategory);
  };

  // Handle delete product
  const handleDeleteProduct = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productApi.deleteProduct(id);
        toast.success('Product deleted successfully');
        loadProducts(currentPage, searchTerm, selectedCategory);
      } catch (err: any) {
        const errorMessage = err.response?.data?.message || 'Failed to delete product';
        toast.error(errorMessage);
      }
    }
  };

  // Load data on component mount
  useEffect(() => {
    loadProducts();
    loadCategories();
  }, []);

  // Format price
  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  // Generate pagination items
  const renderPaginationItems = () => {
    const items = [];
    const maxPages = Math.min(products.totalPages, 10);
    
    for (let page = 0; page < maxPages; page++) {
      items.push(
        <Pagination.Item
          key={page}
          active={page === currentPage}
          onClick={() => handlePageChange(page)}
        >
          {page + 1}
        </Pagination.Item>
      );
    }
    
    return items;
  };

  return (
    <Container fluid className="py-4">
      <Row className="mb-4">
        <Col>
          <h1 className="mb-3">Product Management</h1>
          
          {/* Search and Filter Controls */}
          <Card className="mb-4">
            <Card.Body>
              <Row className="g-3">
                <Col md={4}>
                  <Form onSubmit={handleSearch}>
                    <InputGroup>
                      <Form.Control
                        id="search-input"
                        type="text"
                        placeholder="Search products..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                      />
                      <Button id="search-btn" variant="outline-primary" type="submit">
                        Search
                      </Button>
                    </InputGroup>
                  </Form>
                </Col>
                
                <Col md={3}>
                  <Form.Select
                    id="category-filter"
                    value={selectedCategory}
                    onChange={(e) => handleCategoryChange(e.target.value ? Number(e.target.value) : '')}
                  >
                    <option value="">All Categories</option>
                    {categories.map(category => (
                      <option key={category.id} value={category.id}>
                        {category.name}
                      </option>
                    ))}
                  </Form.Select>
                </Col>
                
                <Col md={3}>
                  <Form.Select
                    value={sortBy}
                    onChange={(e) => handleSortChange(e.target.value)}
                  >
                    <option value="name">Sort by Name</option>
                    <option value="price">Sort by Price</option>
                    <option value="quantity">Sort by Stock</option>
                    <option value="createdAt">Sort by Date</option>
                  </Form.Select>
                </Col>
                
                <Col md={2}>
                  <Button id="add-product-btn" variant="success" className="w-100">
                    Add Product
                  </Button>
                </Col>
              </Row>
            </Card.Body>
          </Card>

          {/* Error Alert */}
          {error && (
            <Alert variant="danger" dismissible onClose={() => setError(null)}>
              {error}
            </Alert>
          )}

          {/* Products Table */}
          <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
              <h5 className="mb-0">Products ({products.totalElements})</h5>
              {loading && <Spinner animation="border" size="sm" />}
            </Card.Header>
            
            <Card.Body className="p-0">
              {products.content.length > 0 ? (
                <>
                  <Table id="products-table" responsive striped hover className="mb-0">
                    <thead className="bg-light">
                      <tr>
                        <th>SKU</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {products.content.map(product => (
                        <tr key={product.id}>
                          <td>
                            <code>{product.sku}</code>
                          </td>
                          <td>
                            <div>
                              <strong className="product-name-link">{product.name}</strong>
                              {product.brand && (
                                <small className="text-muted d-block">{product.brand}</small>
                              )}
                            </div>
                          </td>
                          <td>
                            <span className="product-category badge bg-secondary">
                              {product.categoryName || 'Uncategorized'}
                            </span>
                          </td>
                          <td className="fw-bold">{formatPrice(product.price)}</td>
                          <td>
                            <span className={`badge ${
                              product.quantity === 0 ? 'bg-danger' : 
                              product.quantity <= 10 ? 'bg-warning' : 'bg-success'
                            }`}>
                              {product.quantity} units
                            </span>
                          </td>
                          <td>
                            <span className={`badge ${product.active ? 'bg-success' : 'bg-secondary'}`}>
                              {product.active ? 'Active' : 'Inactive'}
                            </span>
                          </td>
                          <td>
                            <div className="btn-group btn-group-sm">
                              <Button variant="outline-primary" size="sm">
                                View
                              </Button>
                              <Button className="edit-product-btn" variant="outline-secondary" size="sm">
                                Edit
                              </Button>
                              <Button 
                                className="delete-product-btn"
                                variant="outline-danger" 
                                size="sm"
                                onClick={() => handleDeleteProduct(product.id!)}
                              >
                                Delete
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>

                  {/* Pagination */}
                  {products.totalPages > 1 && (
                    <div className="p-3 border-top">
                      <Row className="align-items-center">
                        <Col>
                          <small className="text-muted">
                            Showing {currentPage * products.size + 1} to{' '}
                            {Math.min((currentPage + 1) * products.size, products.totalElements)} of{' '}
                            {products.totalElements} products
                          </small>
                        </Col>
                        <Col xs="auto">
                          <Pagination className="mb-0">
                            <Pagination.Prev
                              disabled={products.first}
                              onClick={() => handlePageChange(currentPage - 1)}
                            />
                            {renderPaginationItems()}
                            <Pagination.Next
                              disabled={products.last}
                              onClick={() => handlePageChange(currentPage + 1)}
                            />
                          </Pagination>
                        </Col>
                      </Row>
                    </div>
                  )}
                </>
              ) : (
                <div className="text-center py-5">
                  {loading ? (
                    <div>
                      <Spinner animation="border" className="mb-3" />
                      <p>Loading products...</p>
                    </div>
                  ) : (
                    <div>
                      <h5 className="text-muted">No products found</h5>
                      <p className="text-muted">
                        {searchTerm || selectedCategory ? 'Try adjusting your search criteria.' : 'Start by adding your first product.'}
                      </p>
                    </div>
                  )}
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ProductList;