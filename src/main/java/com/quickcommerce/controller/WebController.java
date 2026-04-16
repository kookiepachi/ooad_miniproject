package com.quickcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }

    @GetMapping("/register")
    public String register() {
        return "redirect:/register.html";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }

    @GetMapping("/products")
    public String products() {
        return "redirect:/products.html";
    }

    @GetMapping("/cart")
    public String cart() {
        return "redirect:/cart.html";
    }

    @GetMapping("/orders")
    public String orders() {
        return "redirect:/orders.html";
    }

    @GetMapping("/deliveries")
    public String deliveries() {
        return "redirect:/deliveries.html";
    }
}