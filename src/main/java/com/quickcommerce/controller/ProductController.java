package com.quickcommerce.controller;

import com.quickcommerce.dto.ProductDTO;
import com.quickcommerce.entity.Product;
import com.quickcommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Product Controller - RESTful API for product operations
 * Module 1: Inventory & Catalog Management
 * Features: Stock Management, Search, Category Filtering
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Add new product (Store Manager/Admin only)
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.addProduct(product);
            return ResponseEntity.ok(productService.convertToDTO(savedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get product by ID with lazy loading of images
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        try {
            ProductDTO product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Search products by keyword
     * Feature: Search & Suggestions (Minor Feature)
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
        try {
            List<ProductDTO> products = productService.searchProducts(keyword);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get products by category
     * Feature: Category Filtering
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category) {
        try {
            List<ProductDTO> products = productService.getProductsByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get products by subcategory
     */
    @GetMapping("/subcategory/{subcategory}")
    public ResponseEntity<?> getProductsBySubcategory(@PathVariable String subcategory) {
        try {
            List<ProductDTO> products = productService.getProductsBySubcategory(subcategory);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all available products
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableProducts() {
        try {
            List<ProductDTO> products = productService.getAllAvailableProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Update product stock
     * Feature: Real-time stock updates with out of stock handling
     */
    @PutMapping("/{productId}/stock")
    public ResponseEntity<?> updateProductStock(@PathVariable Long productId, @RequestParam Integer newStock) {
        try {
            productService.updateProductStock(productId, newStock);
            return ResponseEntity.ok("Stock updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get low stock products (Admin/Store Manager)
     */
    @GetMapping("/admin/low-stock")
    public ResponseEntity<?> getLowStockProducts() {
        try {
            List<ProductDTO> products = productService.getLowStockProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
