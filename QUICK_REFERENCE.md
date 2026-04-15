# QUICK REFERENCE GUIDE - Quick Commerce Delivery System

## 📁 PROJECT STRUCTURE

```
ooad-mini-project/
│
├── 📄 pom.xml                          # Maven configuration
├── 📄 README.md                         # Project overview & setup
├── 📄 DATABASE_SCHEMA.sql               # MySQL database schema
├── 📄 PHASE_1_REQUIREMENTS.md           # Detailed requirements
├── 📄 UML_DIAGRAMS.md                   # UML diagrams & architecture
│
└── src/main/
    ├── java/com/quickcommerce/
    │   ├── DeliverySystemApplication.java (Main entry point)
    │   │
    │   ├── controller/
    │   │   ├── UserController.java       ← Module 4
    │   │   ├── ProductController.java    ← Module 1
    │   │   ├── CartController.java       ← Module 2
    │   │   ├── OrderController.java      ← Module 2
    │   │   ├── DeliveryController.java   ← Module 3
    │   │   └── WalletController.java     ← Module 4
    │   │
    │   ├── service/
    │   │   ├── UserService.java          ← Module 4
    │   │   ├── ProductService.java       ← Module 1
    │   │   ├── CartService.java          ← Module 2
    │   │   ├── OrderService.java         ← Module 2
    │   │   ├── DiscountService.java      ← Module 2
    │   │   ├── PaymentService.java       ← Module 2, 4
    │   │   ├── DeliveryService.java      ← Module 3
    │   │   ├── WalletService.java        ← Module 4
    │   │   └── JwtService.java           ← Module 4
    │   │
    │   ├── entity/
    │   │   ├── User.java
    │   │   ├── Product.java
    │   │   ├── Order.java
    │   │   ├── OrderItem.java
    │   │   ├── Cart.java               (Removed from DB - in-memory initially)
    │   │   ├── CartItem.java
    │   │   ├── Delivery.java
    │   │   ├── OrderTracking.java
    │   │   ├── Discount.java
    │   │   └── WalletTransaction.java
    │   │
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── ProductRepository.java
    │   │   ├── OrderRepository.java
    │   │   ├── CartRepository.java
    │   │   ├── CartItemRepository.java
    │   │   ├── OrderItemRepository.java
    │   │   ├── DeliveryRepository.java
    │   │   ├── OrderTrackingRepository.java
    │   │   ├── DiscountRepository.java
    │   │   └── WalletTransactionRepository.java
    │   │
    │   ├── dto/                        (Data Transfer Objects)
    │   │   ├── UserDTO.java
    │   │   ├── ProductDTO.java
    │   │   ├── OrderDTO.java
    │   │   ├── OrderItemDTO.java
    │   │   ├── CartItemDTO.java
    │   │   ├── DeliveryDTO.java
    │   │   ├── LoginRequest.java
    │   │   ├── LoginResponse.java
    │   │   ├── RegistrationRequest.java
    │   │   └── CheckoutRequest.java
    │   │
    │   ├── enums/
    │   │   ├── UserRole.java           (CUSTOMER, DELIVERY_PARTNER, ADMIN, STORE_MANAGER)
    │   │   ├── OrderStatus.java        (PENDING, CONFIRMED, PACKING, PACKED, OUT_FOR_DELIVERY, DELIVERED)
    │   │   ├── PaymentMethod.java      (UPI, CARD, COD, WALLET)
    │   │   └── DiscountType.java       (PERCENTAGE, FIXED_AMOUNT, FIRST_ORDER, SEASONAL)
    │   │
    │   └── patterns/                   (Design Patterns)
    │       ├── factory/
    │       │   └── UserFactory.java    🏭 FACTORY METHOD (Creational)
    │       │
    │       ├── proxy/
    │       │   ├── Image.java
    │       │   ├── RealImage.java
    │       │   └── ProxyImage.java     🔒 PROXY PATTERN (Structural)
    │       │
    │       ├── strategy/
    │       │   ├── PaymentStrategy.java
    │       │   ├── UPIPaymentStrategy.java
    │       │   ├── CardPaymentStrategy.java
    │       │   ├── CODPaymentStrategy.java
    │       │   └── WalletPaymentStrategy.java ⚡ STRATEGY (Behavioral)
    │       │
    │       └── observer/
    │           ├── OrderObserver.java
    │           ├── OrderSubject.java
    │           ├── CustomerAppObserver.java
    │           └── DeliveryPartnerAppObserver.java 👁️ OBSERVER (Behavioral)
    │
    └── resources/
        └── application.properties
```

---

## 🏗️ TEAM MEMBER ASSIGNMENTS

### **Member 1: Inventory & Catalog**
**Files to Work On:**
- `entity/Product.java`
- `repository/ProductRepository.java`
- `service/ProductService.java`
- `controller/ProductController.java`
- `patterns/proxy/*` (Proxy Pattern implementation)

**Major Feature:** Stock Management
- Real-time updates
- Out-of-stock logic
- Stock validation

**Minor Feature:** Search & Suggestions
- Keyword search
- Category filtering

---

### **Member 2: Order & Cart**
**Files to Work On:**
- `entity/Cart.java`, `CartItem.java`, `Order.java`, `OrderItem.java`, `Discount.java`
- `repository/CartRepository.java`, `OrderRepository.java`, `DiscountRepository.java`
- `service/CartService.java`, `OrderService.java`, `DiscountService.java`, `PaymentService.java`
- `controller/CartController.java`, `OrderController.java`
- `patterns/strategy/*` (Strategy Pattern for discounts)

**Major Feature:** Order Processing
- Cart management
- Price calculation
- Checkout flow
- Payment processing

**Minor Feature:** Order History & Re-ordering
- View past orders
- One-click reorder

---

### **Member 3: Delivery & Tracking**
**Files to Work On:**
- `entity/Delivery.java`, `OrderTracking.java`
- `repository/DeliveryRepository.java`, `OrderTrackingRepository.java`
- `service/DeliveryService.java`
- `controller/DeliveryController.java`
- `patterns/observer/*` (Observer Pattern implementation)

**Major Feature:** Dispatch System
- Delivery partner assignment
- ETA calculation
- Distance calculation

**Minor Feature:** Live Status Updates
- Order status transitions
- Observer notifications
- Real-time tracking

---

### **Member 4: User & Profile**
**Files to Work On:**
- `entity/User.java`, `WalletTransaction.java`
- `repository/UserRepository.java`, `WalletTransactionRepository.java`
- `service/UserService.java`, `JwtService.java`, `WalletService.java`
- `controller/UserController.java`, `WalletController.java`
- `patterns/factory/UserFactory.java` (Factory Pattern)

**Major Feature:** Authentication & RBAC
- User registration
- Login with JWT
- Role-based access control
- User factory pattern

**Minor Feature:** Wallet & Payments
- Wallet balance management
- Transaction history
- Payment integration

---

## 🔑 KEY CLASSES & METHODS

### **User Management (Module 4)**
```java
UserService.registerUser(RegistrationRequest)   // Register new user
UserService.loginUser(LoginRequest)              // Login and get JWT
UserService.getAllDeliveryPartners()             // Get all partners
UserService.verifyDeliveryPartner(Long id)       // Verify delivery partner

JwtService.generateToken(User)                   // Create JWT token
JwtService.validateToken(String token)           // Validate JWT
JwtService.getUsernameFromToken(String token)    // Extract email from JWT

WalletService.addToWallet(User, BigDecimal, String)
WalletService.deductFromWallet(User, BigDecimal, String)
WalletService.getWalletBalance(User)
WalletService.getTransactionHistory(User)
```

### **Product Management (Module 1)**
```java
ProductService.addProduct(Product)               // Add new product
ProductService.getProductById(Long id)           // Get product (with lazy image loading)
ProductService.searchProducts(String keyword)    // Search products
ProductService.getProductsByCategory(String)    // Filter by category
ProductService.getAllAvailableProducts()         // Get available products
ProductService.updateProductStock(Long, Integer) // Update stock in real-time
ProductService.getLowStockProducts()             // Get low stock alerts

// Stock validation methods
Product.isOutOfStock()                           // Check if stock = 0
Product.decreaseStock(Integer quantity)          // Decrease stock on order
Product.increaseStock(Integer quantity)          // Increase on cancellation
```

### **Cart Management (Module 2)**
```java
CartService.getOrCreateCart(User)                // Get or create cart
CartService.addToCart(User, productId, quantity) // Add item (stock validation)
CartService.removeFromCart(User, productId)      // Remove item
CartService.updateCartItemQuantity(User, productId, quantity)
CartService.clearCart(User)                      // Clear entire cart
CartService.getCartTotal(User)                   // Get total price
```

### **Order Processing (Module 2)**
```java
OrderService.createOrderFromCart(User, CheckoutRequest)  // Create order
OrderService.processOrderPayment(Long orderId, String...)  // Process payment
OrderService.updateOrderStatus(Order, oldStatus, newStatus) // Update status
OrderService.getOrderById(Long orderId)                     // Fetch order
OrderService.getOrderHistory(User)                          // Get past orders
OrderService.getOrderTrackingHistory(Long orderId)          // Get status history
OrderService.reorderFromPreviousOrder(User, previousOrderId) // Reorder

// Price calculation happens automatically:
// subtotal = sum(item prices)
// taxAmount = subtotal * 0.05
// discountAmount = apply discount logic
// deliveryCharge = (subtotal > 100) ? 0 : 49
// totalAmount = subtotal + taxAmount + deliveryCharge - discountAmount
```

### **Discount Management (Module 2)**
```java
DiscountService.applyDiscount(Order, String code)  // Apply discount code
DiscountService.createDiscount(Discount)            // Create new discount
DiscountService.getActiveDiscounts()                // Get all active discounts
DiscountService.incrementUsageCount(String code)    // Track usage
```

### **Payment Processing (Module 2 & 4)**
```java
PaymentService.processPayment(Order, PaymentMethod, ...paymentDetails)
PaymentService.supportsRefund(PaymentMethod)

// Strategy implementations:
UPIPaymentStrategy.processPayment(orderId, amount, reference)
CardPaymentStrategy.processPayment(orderId, amount, reference)
CODPaymentStrategy.processPayment(orderId, amount, reference)
WalletPaymentStrategy.processPayment(orderId, amount, reference)
```

### **Delivery Management (Module 3)**
```java
DeliveryService.assignDeliveryPartner(Order, Long partnerId) // Assign partner
DeliveryService.updateDeliveryStatus(Long deliveryId, OrderStatus) // Update status
DeliveryService.updateDeliveryLocation(Long deliveryId, lat, lon)   // Update GPS
DeliveryService.getPartnerDeliveries(Long partnerId)         // Partner's deliveries
DeliveryService.getActiveDeliveries()                        // Active deliveries

// Automatic calculations:
calculateETA(Delivery)           // ETA = (distance/speed) + buffer
calculateDistance(lat1,lon1,lat2,lon2) // Haversine formula
```

### **Order Tracking (Module 3)**
```java
OrderSubject.attachObserver(OrderObserver)   // Add observer
OrderSubject.detachObserver(OrderObserver)   // Remove observer
OrderSubject.notifyObservers(Order, oldStatus, newStatus) // Notify all observers

// Observers get called when status changes:
CustomerAppObserver.update(...)      // Notify customer
DeliveryPartnerAppObserver.update(...) // Notify delivery partner
```

---

## 💻 API QUICK REFERENCE

### **User APIs (Module 4)**
```bash
# Register
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"pass123","fullName":"John","phoneNumber":"9876543210","role":"CUSTOMER","address":"123 Main","city":"Delhi","zipCode":"110001"}'

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"pass123"}'
```

### **Product APIs (Module 1)**
```bash
# Search products
curl http://localhost:8080/api/products/search?keyword=milk

# Get by category
curl http://localhost:8080/api/products/category/Dairy

# Get available products
curl http://localhost:8080/api/products/available

# Update stock
curl -X PUT "http://localhost:8080/api/products/1/stock?newStock=50"
```

### **Cart APIs (Module 2)**
```bash
# Add to cart
curl -X POST "http://localhost:8080/api/cart/1/add/5?quantity=2"

# Remove from cart
curl -X DELETE "http://localhost:8080/api/cart/1/remove/5"

# Update quantity
curl -X PUT "http://localhost:8080/api/cart/1/update/5?newQuantity=3"

# Get cart total
curl http://localhost:8080/api/cart/1/total
```

### **Order APIs (Module 2)**
```bash
# Create order
curl -X POST "http://localhost:8080/api/orders/create?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"deliveryAddress":"345 Delivery Ln","deliveryCity":"Delhi","deliveryZipCode":"110002","paymentMethod":"UPI"}'

# Get order by ID
curl http://localhost:8080/api/orders/1

# Get order history
curl http://localhost:8080/api/orders/history/1

# Get order tracking
curl http://localhost:8080/api/orders/1/tracking
```

### **Delivery APIs (Module 3)**
```bash
# Assign delivery partner
curl -X POST "http://localhost:8080/api/delivery/assign?orderId=1&deliveryPartnerId=5"

# Update delivery status
curl -X PUT "http://localhost:8080/api/delivery/1/status?newStatus=OUT_FOR_DELIVERY"

# Update location
curl -X PUT "http://localhost:8080/api/delivery/1/location?latitude=28.123&longitude=77.456"

# Get partner deliveries
curl http://localhost:8080/api/delivery/partner/5

# Get active deliveries
curl http://localhost:8080/api/delivery/active
```

---

## 🎯 SOLID PRINCIPLES IMPLEMENTATION

### **Single Responsibility (SRP)**
✅ Each service handles ONE responsibility:
- `CartService` → Cart operations only
- `OrderService` → Order operations only
- `PaymentService` → Payment processing only
- `DeliveryService` → Delivery operations only

### **Open/Closed (OCP)**
✅ System open for extension:
- Add new `DiscountType` without modifying existing code
- Add new `PaymentStrategy` implementation without changing strategy context
- Add new `UserRole` easily via factory pattern

### **Liskov Substitution (LSP)**
✅ Subtypes substitutable for base types:
- All payment strategies implement `PaymentStrategy`
- All user types (Customer, Partner, Admin) work as `User`

### **Interface Segregation (ISP)**
✅ Clients not forced to use unused methods:
- `PaymentStrategy` has only payment methods
- `OrderObserver` has only observer methods

### **Dependency Inversion (DIP)**
✅ Depend on abstractions, not concretions:
- Controllers depend on Services (interfaces)
- Services depend on Repositories (interfaces)
- Payment processing depends on Strategy interface

---

## 🏭 DESIGN PATTERNS QUICK REFERENCE

### **Factory Method (Creational)**
```java
// Creates different user types without specifying classes
User customer = UserFactory.createUser(
    UserRole.CUSTOMER, 
    email, password, name, phone
);
```

### **Proxy Pattern (Structural)**
```java
// Lazy loads high-res images only when accessed
Image lazyImage = new ProxyImage("large-image.jpg", imageSize);
lazyImage.display(); // Loads only NOW
```

### **Strategy Pattern (Behavioral)**
```java
// Swaps payment methods at runtime
PaymentStrategy strategy = 
    paymentMethod == PaymentMethod.UPI ? 
    new UPIPaymentStrategy(upiId, pin) :
    new CardPaymentStrategy(cardNumber, ...);
strategy.processPayment(order, amount);
```

### **Observer Pattern (Behavioral)**
```java
// Notifies multiple apps when order status changes
orderSubject.attachObserver(new CustomerAppObserver());
orderSubject.attachObserver(new DeliveryPartnerAppObserver());
orderSubject.notifyObservers(order, oldStatus, newStatus);
// Both observers are automatically notified!
```

---

## 📊 DATABASE TABLES OVERVIEW

| Module | Tables | Purpose |
|--------|--------|---------|
| **User (4)** | users | All user roles and profiles |
| | wallet_transactions | Wallet debit/credit history |
| **Inventory (1)** | products | Product catalog with stock |
| **Cart (2)** | carts | Shopping carts |
| | cart_items | Items in each cart |
| **Order (4)** | orders | Order records |
| | order_items | Items ordered |
| | discounts | Discount codes |
| | order_tracking | Status change history |
| **Delivery (2)** | deliveries | Delivery assignments |
| | order_tracking | Status history (shared) |

**Total Tables:** 10
**Relationships:** Orders ← (1:N) → OrderItems ← (1:N) → Products

---

## ⚙️ GETTING STARTED

1. **Clone the project**
   ```bash
   cd ~/Desktop/ooad\ mini\ project
   ```

2. **Create database**
   ```bash
   mysql -u root -p < DATABASE_SCHEMA.sql
   ```

3. **Update properties**
   - Edit `src/main/resources/application.properties`
   - Set your database credentials

4. **Build project**
   ```bash
   mvn clean install
   ```

5. **Run application**
   ```bash
   mvn spring-boot:run
   ```

6. **Test APIs**
   ```bash
   curl http://localhost:8080/api/products/available
   ```

---

## 📝 KEY CONCEPTS TO REMEMBER

1. **MVC Architecture:** Model (Entity) → View (DTO) → Controller (API)
2. **Repository Pattern:** Abstract data access layer
3. **Service Layer:** Business logic layer between Controller and Repository
4. **DTOs:** Encapsulate data transfer between layers
5. **Design Patterns:** Proven solutions for common problems
6. **SOLID Principles:** Write clean, maintainable code
7. **JWT Authentication:** Stateless, token-based security
8. **Observer Pattern:** Publish-Subscribe mechanism for notifications

---

## ✅ CHECKLIST FOR SUBMISSION

- [ ] All 4 modules implemented
- [ ] 4 design patterns demonstrated
- [ ] SOLID principles followed
- [ ] Database schema created
- [ ] All APIs tested and working
- [ ] README.md comprehensive
- [ ] UML diagrams provided
- [ ] Code well-commented
- [ ] Error handling implemented
- [ ] Sample data included
- [ ] Phase 1 requirements document
- [ ] Project builds successfully
- [ ] No compilation errors

---

**Happy Coding! 🚀**

For detailed information, refer to:
- `README.md` - Project overview
- `PHASE_1_REQUIREMENTS.md` - Detailed specifications
- `UML_DIAGRAMS.md` - Architecture diagrams
- `DATABASE_SCHEMA.sql` - Database design
