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
                productRepository.save(createProduct("Butter Chicken", "Creamy butter chicken with naan bread and rice.", "main-course", "chicken", "https://via.placeholder.com/260x180.png?text=Butter+Chicken", 320.0, 50));
                productRepository.save(createProduct("Paneer Tikka", "Grilled cottage cheese with tandoori spices.", "appetizers", "vegetarian", "https://via.placeholder.com/260x180.png?text=Paneer+Tikka", 240.0, 40));
                productRepository.save(createProduct("Biryani Special", "Fragrant basmati rice with tender meat and spices.", "main-course", "rice", "https://via.placeholder.com/260x180.png?text=Biryani", 380.0, 35));
                productRepository.save(createProduct("Masala Dosa", "Crispy rice crepe with potato and lentil filling.", "breakfast", "vegetarian", "https://via.placeholder.com/260x180.png?text=Dosa", 180.0, 45));
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

    private Product createProduct(String name, String description, String category, String subcategory, String imageUrl, double price, int stock) {
        Product product = new Product();
        product.setProductName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setImageUrl(imageUrl);
        product.setPrice(java.math.BigDecimal.valueOf(price));
        product.setStock(stock);
        product.setIsAvailable(true);
        return product;
    }
}
