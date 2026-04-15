package com.quickcommerce.patterns.proxy;

/**
 * PROXY PATTERN (Structural)
 * Real image interface for loading product images
 * Supports lazy loading - actual image is loaded only when accessed
 */
public interface Image {
    void display();
}
