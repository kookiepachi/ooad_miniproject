package com.quickcommerce.repository;

import com.quickcommerce.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByDiscountCode(String discountCode);
    List<Discount> findByIsActive(Boolean isActive);
    List<Discount> findByIsFirstOrderOnly(Boolean isFirstOrderOnly);
}
