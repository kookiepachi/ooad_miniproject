package com.quickcommerce.repository;

import com.quickcommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findBySubcategory(String subcategory);
    List<Product> findByIsAvailable(Boolean isAvailable);
    
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.isAvailable = true AND p.stock > 0")
    List<Product> findAvailableByCategory(@Param("category") String category);
    
    List<Product> findByStockLessThan(Integer stock);
    
    Optional<Product> findByBarcode(String barcode);
}
