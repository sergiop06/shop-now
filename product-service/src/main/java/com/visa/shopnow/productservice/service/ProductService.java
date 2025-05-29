package com.visa.shopnow.productservice.service;

import com.visa.shopnow.productservice.dto.CategoryRequest;
import com.visa.shopnow.productservice.dto.CategoryResponse;
import com.visa.shopnow.productservice.dto.ProductRequest;
import com.visa.shopnow.productservice.dto.ProductResponse;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategoryId(Long categoryId);
    List<ProductResponse> getProductsBySellerId(Long sellerId);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    void updateProductStock(Long productId, Integer quantityChange);

    // Category Operations
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
