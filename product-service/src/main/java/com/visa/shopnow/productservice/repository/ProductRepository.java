package com.visa.shopnow.productservice.repository;

import com.visa.shopnow.productservice.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    boolean existsByName(String name);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findBySellerId(Long sellerId);
}
