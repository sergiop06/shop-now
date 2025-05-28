package com.visa.shopnow.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    private String description;

    @NotNull(message = "Product price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
    private BigDecimal price;

    @NotNull(message = "Stock quantity cannot be null")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotNull(message = "Seller ID cannot be null")
    private Long sellerId; // ID of the seller from User Service

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    private String imageUrl;
}

