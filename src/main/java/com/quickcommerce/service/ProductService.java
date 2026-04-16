package com.quickcommerce.service;

import com.quickcommerce.dto.ProductDTO;
import com.quickcommerce.entity.Product;
import com.quickcommerce.patterns.proxy.Image;
import com.quickcommerce.patterns.proxy.ProxyImage;
import com.quickcommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Service - Manages product catalog and inventory
 * Module 1: Inventory & Catalog
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Add a new product
     */
    public Product addProduct(Product product) {
        product.setIsAvailable(true);
        return productRepository.save(product);
    }

    /**
     * Get product by ID with Lazy Loading using Proxy Pattern
     */
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        
        // Using Proxy Pattern for lazy loading of product images
        if (product.getImageUrl() != null) {
            Image lazyImage = new ProxyImage(product.getImageUrl(), 102400); // 100KB
            lazyImage.display(); // Loads only when accessed
        }

        return convertToDTO(product);
    }

    /**
     * Search products by keyword 
     * Feature: Search & Suggestions (Minor Feature for Member 1)
     */
    public List<ProductDTO> searchProducts(String keyword) {
        List<Product> products = productRepository.searchByKeyword(keyword);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get products by category with stock filtering
     * Feature: Category Filtering
     */
    public List<ProductDTO> getProductsByCategory(String category) {
        List<Product> products = productRepository.findAvailableByCategory(category);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get products by subcategory
     */
    public List<ProductDTO> getProductsBySubcategory(String subcategory) {
        List<Product> products = productRepository.findBySubcategory(subcategory);
        return products.stream()
                .filter(p -> p.getIsAvailable() && !p.isOutOfStock())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all available products
     */
    public List<ProductDTO> getAllAvailableProducts() {
        List<Product> products = productRepository.findByIsAvailable(true);
        return products.stream()
                .filter(p -> !p.isOutOfStock())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update product stock - Real-time updates
     * Feature: Stock Management with out of stock logic
     */
    public void updateProductStock(Long productId, Integer newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        product.setStock(newStock);
        
        // Check if product is now out of stock
        if (product.isOutOfStock()) {
            product.setIsAvailable(false);
            System.out.println("[Stock Alert] Product " + product.getProductName() + " is out of stock!");
        } else {
            product.setIsAvailable(true);
        }
        
        productRepository.save(product);
    }

    /**
     * Decrease product stock
     */
    public void decreaseProductStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
        }

        product.decreaseStock(quantity);
        productRepository.save(product);
    }

    /**
     * Increase product stock
     */
    public void increaseProductStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        product.increaseStock(quantity);
        productRepository.save(product);
    }

    /**
     * Get low stock products (Below minimum level)
     */
    public List<ProductDTO> getLowStockProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(p -> p.getStock() <= p.getMinStockLevel())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Product entity to DTO
     */
    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.productId = product.getProductId();
        dto.productName = product.getProductName();
        dto.description = product.getDescription();
        dto.price = product.getPrice();
        dto.stock = product.getStock();
        dto.category = product.getCategory();
        dto.subcategory = product.getSubcategory();
        dto.imageUrl = product.getImageUrl();
        dto.rating = product.getRating();
        dto.reviewCount = product.getReviewCount();
        dto.isAvailable = product.getIsAvailable();
        return dto;
    }
}
