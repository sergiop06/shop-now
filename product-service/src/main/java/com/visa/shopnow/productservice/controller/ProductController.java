package com.visa.shopnow.productservice.controller;

import com.visa.shopnow.productservice.dto.CategoryRequest;
import com.visa.shopnow.productservice.dto.CategoryResponse;
import com.visa.shopnow.productservice.dto.ProductApiResponse;
import com.visa.shopnow.productservice.dto.ProductRequest;
import com.visa.shopnow.productservice.dto.ProductResponse;
import com.visa.shopnow.productservice.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductResponse newProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductApiResponse.success(newProduct, "Product created successfully."));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ProductApiResponse.success(product, "Product retrieved successfully."));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(ProductApiResponse.success(products, "Products retrieved successfully."));
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<ProductApiResponse<List<ProductResponse>>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponse> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(ProductApiResponse.success(products, "Products retrieved successfully."));
    }

    @GetMapping("/products/seller/{sellerId}")
    public ResponseEntity<ProductApiResponse<List<ProductResponse>>> getProductsBySellerId(@PathVariable Long sellerId) {
        List<ProductResponse> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(ProductApiResponse.success(products, "Product retrieved successfully."));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(ProductApiResponse.success(updatedProduct, "Product updated successfully."));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ProductApiResponse.success("Product deleted successfully."));
    }

    @PatchMapping("/products/{id}/stock")
    public ResponseEntity<ProductApiResponse<Void>> updateProductStock(
            @PathVariable Long id,
            @RequestParam Integer quantityChange) {
        productService.updateProductStock(id, quantityChange);
        return ResponseEntity.ok(ProductApiResponse.success("Product stock updated successfully."));
    }

    @PostMapping("/categories")
    public ResponseEntity<ProductApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse newCategory = productService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductApiResponse.success(newCategory, "Category created successfully."));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ProductApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = productService.getCategoryById(id);
        return ResponseEntity.ok(ProductApiResponse.success(category, "Category retrieved successfully."));
    }

    @GetMapping("/categories")
    public ResponseEntity<ProductApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = productService.getAllCategories();
        return ResponseEntity.ok(ProductApiResponse.success(categories, "Category retrieved successfully."));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ProductApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse updatedCategory = productService.updateCategory(id, request);
        return ResponseEntity.ok(ProductApiResponse.success(updatedCategory, "Category updated successfully."));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ProductApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        productService.deleteCategory(id);
        return ResponseEntity.ok(ProductApiResponse.success("Category deleted successfully."));
    }
}
