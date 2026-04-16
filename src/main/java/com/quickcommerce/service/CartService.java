package com.quickcommerce.service;

import com.quickcommerce.entity.Cart;
import com.quickcommerce.entity.CartItem;
import com.quickcommerce.entity.Product;
import com.quickcommerce.entity.User;
import com.quickcommerce.repository.CartRepository;
import com.quickcommerce.repository.CartItemRepository;
import com.quickcommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Cart Service - Manages shopping cart
 * Feature: Cart logic and management
 */
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get or create cart for user
     */
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUserUserId(user.getUserId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setTotalPrice(BigDecimal.ZERO);
                    return cartRepository.save(cart);
                });
    }

    /**
     * Add item to cart with stock validation
     */
    public void addToCart(User user, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        // Stock validation - only add if stock > 0
        if (product.isOutOfStock()) {
            throw new RuntimeException("Product " + product.getProductName() + " is out of stock!");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock! Available: " + product.getStock() + ", Requested: " + quantity);
        }

        Cart cart = getOrCreateCart(user);

        CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (existingItem != null) {
            // Update quantity if item already in cart
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.updatePrice();
            cartItemRepository.save(existingItem);
        } else {
            // Add new item to cart
            CartItem cartItem = new CartItem(cart, product, quantity);
            cartItem.updatePrice();
            cartItemRepository.save(cartItem);
            cart.getCartItems().add(cartItem);
        }

        cart.calculateTotal();
        cartRepository.save(cart);
    }

    /**
     * Remove item from cart
     */
    public void removeFromCart(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not in cart!"));

        cartItemRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);
        cart.calculateTotal();
        cartRepository.save(cart);
    }

    /**
     * Update quantity of item in cart
     */
    public void updateCartItemQuantity(User user, Long productId, Integer newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        // Validate product is available and has stock
        if (product.isOutOfStock() || product.getStock() < newQuantity) {
            throw new RuntimeException("Insufficient stock for update!");
        }

        Cart cart = getOrCreateCart(user);
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not in cart!"));

        cartItem.setQuantity(newQuantity);
        cartItem.updatePrice();
        cartItemRepository.save(cartItem);

        cart.calculateTotal();
        cartRepository.save(cart);
    }

    /**
     * Clear cart
     */
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    /**
     * Get total cart price
     */
    public BigDecimal getCartTotal(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getTotalPrice();
    }
}
