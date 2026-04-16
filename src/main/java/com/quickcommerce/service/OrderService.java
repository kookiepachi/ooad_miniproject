package com.quickcommerce.service;

import com.quickcommerce.dto.CheckoutRequest;
import com.quickcommerce.dto.OrderDTO;
import com.quickcommerce.entity.*;
import com.quickcommerce.enums.OrderStatus;
import com.quickcommerce.enums.PaymentMethod;
import com.quickcommerce.patterns.observer.*;
import com.quickcommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Order Service - Handles order processing and checkout
 * Feature: Order Processing with cart logic, price calculation (discounts/taxes), checkout flow
 * Integrates Observer Pattern for order status notifications
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderTrackingRepository orderTrackingRepository;

    private OrderSubject orderSubject = new OrderSubject();

    public OrderService() {
        // Initialize observers
        this.orderSubject.attachObserver(new CustomerAppObserver());
        this.orderSubject.attachObserver(new DeliveryPartnerAppObserver());
    }

    /**
     * Create order from cart with price calculation
     * Feature: Price calculation with discounts, taxes, delivery charges
     */
    public Order createOrderFromCart(User customer, CheckoutRequest checkoutRequest) {
        Cart cart = cartService.getOrCreateCart(customer);

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(checkoutRequest.getDeliveryAddress());
        order.setDeliveryCity(checkoutRequest.getDeliveryCity());
        order.setDeliveryZipCode(checkoutRequest.getDeliveryZipCode());
        order.setSpecialInstructions(checkoutRequest.getSpecialInstructions());
        order.setPaymentMethod(PaymentMethod.valueOf(checkoutRequest.getPaymentMethod()));
        order.setTrackingNumber(generateTrackingNumber());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(30));

        // Calculate order items and subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtTime(cartItem.getProduct().getPrice());
            orderItem.calculateTotal();

            order.getOrderItems().add(orderItem);
            subtotal = subtotal.add(orderItem.getTotalPrice());
        }

        order.setSubtotal(subtotal);

        // Apply tax (5%)
        BigDecimal taxAmount = subtotal.multiply(new BigDecimal("0.05"));
        order.setTaxAmount(taxAmount);

        // Apply discount if provided
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (checkoutRequest.getDiscountCode() != null && !checkoutRequest.getDiscountCode().isEmpty()) {
            try {
                discountAmount = discountService.applyDiscount(order, checkoutRequest.getDiscountCode());
                discountService.incrementUsageCount(checkoutRequest.getDiscountCode());
            } catch (Exception e) {
                System.out.println("Could not apply discount: " + e.getMessage());
            }
        }
        order.setDiscountAmount(discountAmount);

        // Set delivery charge (Simple logic: Free over Rs. 100)
        BigDecimal deliveryCharge = order.getSubtotal().compareTo(new BigDecimal("100")) > 0 ? 
                                    BigDecimal.ZERO : new BigDecimal("49");
        order.setDeliveryCharge(deliveryCharge);

        // Calculate total amount
        BigDecimal totalAmount = order.getSubtotal()
                .add(order.getTaxAmount())
                .add(order.getDeliveryCharge())
                .subtract(order.getDiscountAmount());
        order.setTotalAmount(totalAmount);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Decrease product stock after order creation
        for (CartItem cartItem : cart.getCartItems()) {
            productService.decreaseProductStock(cartItem.getProduct().getProductId(), cartItem.getQuantity());
        }

        // Clear cart after order creation
        cartService.clearCart(customer);

        // Notify observers about order creation
        updateOrderStatus(savedOrder, OrderStatus.PENDING, OrderStatus.CONFIRMED);

        return savedOrder;
    }

    /**
     * Process payment and confirm order
     */
    public void processOrderPayment(Long orderId, String... paymentDetails) {
        Order order = getOrderById(orderId);
        paymentService.processPayment(order, order.getPaymentMethod(), paymentDetails);
        orderRepository.save(order);
    }

    /**
     * Update order status and notify observers
     * Feature: Live Status Updates - Observer Pattern in action
     */
    public void updateOrderStatus(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Record status change in tracking
        OrderTracking tracking = new OrderTracking();
        tracking.setOrder(order);
        tracking.setPreviousStatus(oldStatus);
        tracking.setCurrentStatus(newStatus);
        tracking.setStatusChangedAt(LocalDateTime.now());
        orderTrackingRepository.save(tracking);

        // Notify all observers (Observer Pattern)
        orderSubject.notifyObservers(order, oldStatus, newStatus);
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    /**
     * Get order history for a customer
     * Feature: Order History and re-ordering functionality
     */
    public List<OrderDTO> getOrderHistory(User customer) {
        List<Order> orders = orderRepository.findByCustomer(customer);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pending orders
     */
    public List<OrderDTO> getPendingOrders() {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.PENDING);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get order tracking history
     */
    public List<OrderTracking> getOrderTrackingHistory(Long orderId) {
        Order order = getOrderById(orderId);
        return orderTrackingRepository.findByOrderOrderByStatusChangedAtDesc(order);
    }

    /**
     * Reorder functionality
     */
    public Order reorderFromPreviousOrder(User customer, Long previousOrderId) {
        Order previousOrder = getOrderById(previousOrderId);

        if (!previousOrder.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new RuntimeException("Unauthorized access!");
        }

        // Add previous order items back to cart
        for (OrderItem orderItem : previousOrder.getOrderItems()) {
            cartService.addToCart(customer, orderItem.getProduct().getProductId(), orderItem.getQuantity());
        }

        // Create checkout request with same delivery details
        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setDeliveryAddress(previousOrder.getDeliveryAddress());
        checkoutRequest.setDeliveryCity(previousOrder.getDeliveryCity());
        checkoutRequest.setDeliveryZipCode(previousOrder.getDeliveryZipCode());
        checkoutRequest.setPaymentMethod(previousOrder.getPaymentMethod().toString());

        return createOrderFromCart(customer, checkoutRequest);
    }

    /**
     * Convert Order entity to DTO
     */
    public OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.orderId = order.getOrderId();
        dto.userId = order.getCustomer() != null ? order.getCustomer().getUserId() : null;
        dto.subtotal = order.getSubtotal();
        dto.discountAmount = order.getDiscountAmount();
        dto.deliveryCharge = order.getDeliveryCharge();
        dto.totalAmount = order.getTotalAmount();
        dto.estimatedDeliveryFee = order.getDeliveryCharge();
        dto.orderStatus = order.getOrderStatus().toString();
        dto.paymentMethod = order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null;
        dto.deliveryAddress = order.getDeliveryAddress();
        dto.deliveryCity = order.getDeliveryCity();
        dto.deliveryZipCode = order.getDeliveryZipCode();
        dto.trackingNumber = order.getTrackingNumber();
        dto.createdAt = order.getCreatedAt() != null ? order.getCreatedAt().toString() : null;
        dto.specialInstructions = order.getSpecialInstructions();
        dto.deliveryPartnerId = order.getDeliveryPartner() != null ? order.getDeliveryPartner().getUserId() : null;
        dto.items = order.getOrderItems().stream().map(item -> {
            java.util.Map<String, Object> itemMap = new java.util.HashMap<>();
            itemMap.put("productName", item.getProduct() != null ? item.getProduct().getProductName() : "");
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("priceAtTime", item.getPriceAtTime());
            itemMap.put("totalPrice", item.getTotalPrice());
            return itemMap;
        }).toList();
        return dto;
    }

    /**
     * Generate unique tracking number
     */
    private String generateTrackingNumber() {
        return "QC-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
