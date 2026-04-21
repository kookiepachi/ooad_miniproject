package com.quickcommerce;

import com.quickcommerce.entity.Product;
import com.quickcommerce.entity.User;
import com.quickcommerce.enums.UserRole;
import com.quickcommerce.repository.ProductRepository;
import com.quickcommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.quickcommerce"})
public class DeliverySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliverySystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedData(ProductRepository productRepository, UserRepository userRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(createProduct(
                    "Cheeseburger",
                    "Juicy beef patty with melted cheddar and crispy lettuce.",
                    "Popular Restaurants",
                    "Burger House",
                    "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=900&q=80",
                    250.0,
                    57,
                    4.8,
                    449
                ));
                productRepository.save(createProduct(
                    "Margherita Pizza",
                    "Classic pizza with fresh mozzarella, basil, and tomato sauce.",
                    "Popular Restaurants",
                    "Italian Kitchen",
                    "https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=900&q=80",
                    350.0,
                    46,
                    4.2,
                    92
                ));
                productRepository.save(createProduct(
                    "Biryani Special",
                    "Fragrant basmati rice with tender meat and aromatic spices.",
                    "Popular Restaurants",
                    "Royal Biryani",
                    "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?auto=format&fit=crop&w=900&q=80",
                    380.0,
                    45,
                    4.3,
                    308
                ));
                productRepository.save(createProduct(
                    "Paneer Tikka",
                    "Grilled cottage cheese with tandoori spices and yogurt sauce.",
                    "Popular Restaurants",
                    "Veg Delight",
                    "https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=900&q=80",
                    240.0,
                    65,
                    4.8,
                    234
                ));
                productRepository.save(createProduct(
                    "Chocolate Cake",
                    "Rich chocolate sponge layered with smooth ganache frosting.",
                    "Popular Restaurants",
                    "Sweet Tooth",
                    "https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&w=900&q=80",
                    150.0,
                    22,
                    4.6,
                    118
                ));
                productRepository.save(createProduct(
                    "Fresh Smoothie",
                    "A chilled berry smoothie blended with yogurt and fresh fruit.",
                    "Popular Restaurants",
                    "Juice Junction",
                    "https://images.unsplash.com/photo-1553530666-ba11a7da3888?auto=format&fit=crop&w=900&q=80",
                    120.0,
                    31,
                    4.4,
                    73
                ));
                productRepository.save(createProduct(
                    "Garlic Bread",
                    "Toasted garlic bread finished with herbs and butter.",
                    "Popular Restaurants",
                    "Italian Kitchen",
                    "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=900&q=80",
                    80.0,
                    28,
                    4.1,
                    54
                ));
            }

            if (!userRepository.existsByEmail("guest@quickcommerce.com")) {
                User user = new User();
                user.setEmail("guest@quickcommerce.com");
                user.setPassword("guest123");
                user.setFullName("Guest Customer");
                user.setPhoneNumber("9999999999");
                user.setRole(UserRole.CUSTOMER);
                user.setAddress("123 Quick St");
                user.setCity("Quick City");
                user.setZipCode("123456");
                userRepository.save(user);
            }
        };
    }

    private Product createProduct(
        String name,
        String description,
        String category,
        String subcategory,
        String imageUrl,
        double price,
        int stock,
        double rating,
        int reviewCount
    ) {
        Product product = new Product();
        product.setProductName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setImageUrl(imageUrl);
        product.setPrice(java.math.BigDecimal.valueOf(price));
        product.setStock(stock);
        product.setRating(rating);
        product.setReviewCount(reviewCount);
        product.setIsAvailable(true);
        return product;
    }
}
