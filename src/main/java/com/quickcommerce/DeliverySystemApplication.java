package com.quickcommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.quickcommerce"})
public class DeliverySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliverySystemApplication.class, args);
    }
}
