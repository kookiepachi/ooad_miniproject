package com.quickcommerce.dto;

import java.math.BigDecimal;

public class ProductDTO {
    public Long productId;
    public String productName;
    public String description;
    public BigDecimal price;
    public Integer stock;
    public String category;
    public String subcategory;
    public String imageUrl;
    public Double rating  = 0.0;
    public Integer reviewCount = 0;
    public Boolean isAvailable = true;
    
    public ProductDTO() {}
    public ProductDTO(Long productId, String productName, String description, BigDecimal price, Integer stock, String category, String subcategory, String imageUrl, Double rating, Integer reviewCount, Boolean isAvailable) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.subcategory = subcategory;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.isAvailable = isAvailable;
    }
}