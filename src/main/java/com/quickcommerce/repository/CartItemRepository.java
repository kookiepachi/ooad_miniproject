package com.quickcommerce.repository;

import com.quickcommerce.entity.CartItem;
import com.quickcommerce.entity.Cart;
import com.quickcommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Integer countByCart(Cart cart);
}
