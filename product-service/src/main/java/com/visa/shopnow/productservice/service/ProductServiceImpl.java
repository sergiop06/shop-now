package com.visa.shopnow.productservice.service;

import com.visa.shopnow.productservice.dto.CategoryRequest;
import com.visa.shopnow.productservice.dto.CategoryResponse;
import com.visa.shopnow.productservice.dto.ProductRequest;
import com.visa.shopnow.productservice.dto.ProductResponse;
import com.visa.shopnow.productservice.exception.ErrorCode;
import com.visa.shopnow.productservice.exception.ProductServiceException;
import com.visa.shopnow.productservice.model.Category;
import com.visa.shopnow.productservice.model.Product;
import com.visa.shopnow.productservice.repository.CategoryRepository;
import com.visa.shopnow.productservice.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new ProductServiceException(ErrorCode.PRODUCT_NAME_ALREADY_EXISTS,
                    "Product with name '" + request.getName() + "' already exists.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ProductServiceException(ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with ID: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .sellerId(request.getSellerId())
                .category(category)
                .imageUrl(request.getImageUrl())
                .build();

        Product savedProduct = productRepository.save(product);
        return mapProductToProductResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductServiceException(ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with ID: " + id, HttpStatus.NOT_FOUND));
        return mapProductToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ProductServiceException(ErrorCode.CATEGORY_NOT_FOUND,
                    "Category not found with ID: " + categoryId, HttpStatus.NOT_FOUND);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductServiceException(ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with ID: " + id, HttpStatus.NOT_FOUND));

        if (request.getName() != null && !request.getName().isBlank() && !existingProduct.getName().equals(request.getName())) {
            if (productRepository.existsByName(request.getName())) {
                throw new ProductServiceException(ErrorCode.PRODUCT_NAME_ALREADY_EXISTS,
                        "Product with name '" + request.getName() + "' already exists.");
            }
            existingProduct.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingProduct.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            existingProduct.setPrice(request.getPrice());
        }
        if (request.getStockQuantity() != null) {
            existingProduct.setStockQuantity(request.getStockQuantity());
        }
        if (request.getImageUrl() != null) {
            existingProduct.setImageUrl(request.getImageUrl());
        }

        if (request.getSellerId() != null) {
            existingProduct.setSellerId(request.getSellerId());
        }

        if (request.getCategoryId() != null && !request.getCategoryId().equals(existingProduct.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ProductServiceException(ErrorCode.CATEGORY_NOT_FOUND,
                            "Category not found with ID: " + request.getCategoryId()));
            existingProduct.setCategory(newCategory);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return mapProductToProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductServiceException(ErrorCode.PRODUCT_NOT_FOUND,
                    "Product not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateProductStock(Long productId, Integer quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceException(ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with ID: " + productId, HttpStatus.NOT_FOUND));

        int newQuantity = product.getStockQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new ProductServiceException(ErrorCode.INSUFFICIENT_STOCK,
                    "Insufficient stock for product ID: " + productId + ". Available: " + product.getStockQuantity());
        }
        product.setStockQuantity(newQuantity);
        productRepository.save(product);
    }

    // --- Category Operations ---

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ProductServiceException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS,
                    "Category with name '" + request.getName() + "' already exists.");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapCategoryToCategoryResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ProductServiceException(ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with ID: " + id, HttpStatus.NOT_FOUND));
        return mapCategoryToCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapCategoryToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ProductServiceException(ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with ID: " + id, HttpStatus.NOT_FOUND));

        if (request.getName() != null && !request.getName().isBlank() && !existingCategory.getName().equals(request.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new ProductServiceException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS,
                        "Category with name '" + request.getName() + "' already exists.");
            }
            existingCategory.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingCategory.setDescription(request.getDescription());
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return mapCategoryToCategoryResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ProductServiceException(ErrorCode.CATEGORY_NOT_FOUND,
                    "Category not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }

    // --- Mappers ---

    private ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sellerId(product.getSellerId())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private CategoryResponse mapCategoryToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
