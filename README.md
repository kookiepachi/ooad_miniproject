# Quick Commerce Delivery System - OOAD Mini Project

## Project Overview

This is a **comprehensive Java Spring Boot application** implementing an online delivery system (like Zepto - Quick Commerce) following **Object-Oriented Analysis and Design (OOAD) principles**. The project is designed for **4 team members** with each managing a distinct module with one major and one minor feature.

## Project Highlights

- ✅ **Spring Boot 3.1.5** with MVC Architecture
- ✅ **RESTful APIs** with JWT Authentication
- ✅ **MySQL Database** with optimal schema
- ✅ **Design Patterns** (Factory, Proxy, Strategy, Observer)
- ✅ **SOLID Principles** (SRP, OCP, LSP, DIP, ISP)
- ✅ **Role-Based Access Control** (RBAC)
- ✅ **Real-time Order Tracking** with Observer Pattern
- ✅ **Multiple Payment Methods** with Strategy Pattern

---

## Team Division & Module Allocation

### **Team Structure (4 Members)**

| Member | Module | Major Feature | Minor Feature |
|--------|--------|---------------|---------------|
| **Member 1** | Inventory & Catalog | Stock Management (Real-time, Out-of-Stock Logic, Category Filtering) | Search & Suggestions |
| **Member 2** | Order & Cart | Order Processing (Cart Logic, Price Calculation, Checkout Flow) | Order History & Re-ordering |
| **Member 3** | Delivery & Tracking | Dispatch System (Partner Assignment, ETA Calculation) | Live Status Updates |
| **Member 4** | User & Profile | Authentication & RBAC (Multi-role Login) | Wallet/Payments |

---

## Project Structure

```
ooad-mini-project/
├── src/
│   ├── main/
│   │   ├── java/com/quickcommerce/
│   │   │   ├── DeliverySystemApplication.java          # Main Spring Boot App
│   │   │   ├── controller/                             # REST Controllers
│   │   │   │   ├── UserController.java                 # Module 4
│   │   │   │   ├── ProductController.java              # Module 1
│   │   │   │   ├── CartController.java                 # Module 2
│   │   │   │   ├── OrderController.java                # Module 2
│   │   │   │   ├── DeliveryController.java             # Module 3
│   │   │   │   └── WalletController.java               # Module 4
│   │   │   ├── service/                                # Business Logic (Services)
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── CartService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── DiscountService.java
│   │   │   │   ├── PaymentService.java
│   │   │   │   ├── DeliveryService.java
│   │   │   │   ├── WalletService.java
│   │   │   │   └── JwtService.java
│   │   │   ├── entity/                                 # JPA Entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Delivery.java
│   │   │   │   ├── OrderTracking.java
│   │   │   │   ├── Discount.java
│   │   │   │   └── WalletTransaction.java
│   │   │   ├── repository/                             # Spring Data JPA Repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── CartRepository.java
│   │   │   │   ├── DeliveryRepository.java
│   │   │   │   ├── DiscountRepository.java
│   │   │   │   └── WalletTransactionRepository.java
│   │   │   ├── dto/                                    # Data Transfer Objects
│   │   │   │   ├── UserDTO.java
│   │   │   │   ├── ProductDTO.java
│   │   │   │   ├── OrderDTO.java
│   │   │   │   ├── CartItemDTO.java
│   │   │   │   ├── DeliveryDTO.java
│   │   │   │   ├── LoginRequest/Response.java
│   │   │   │   └── CheckoutRequest.java
│   │   │   ├── enums/                                  # Enumerations
│   │   │   │   ├── UserRole.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   ├── PaymentMethod.java
│   │   │   │   └── DiscountType.java
│   │   │   └── patterns/                               # Design Patterns
│   │   │       ├── factory/
│   │   │       │   └── UserFactory.java                # Creational: Factory Method
│   │   │       ├── proxy/
│   │   │       │   ├── Image.java
│   │   │       │   ├── RealImage.java
│   │   │       │   └── ProxyImage.java                 # Structural: Proxy Pattern
│   │   │       ├── strategy/
│   │   │       │   ├── PaymentStrategy.java
│   │   │       │   ├── UPIPaymentStrategy.java
│   │   │       │   ├── CardPaymentStrategy.java
│   │   │       │   ├── CODPaymentStrategy.java
│   │   │       │   └── WalletPaymentStrategy.java      # Behavioral: Strategy Pattern
│   │   │       └── observer/
│   │   │           ├── OrderObserver.java
│   │   │           ├── OrderSubject.java
│   │   │           ├── CustomerAppObserver.java
│   │   │           └── DeliveryPartnerAppObserver.java # Behavioral: Observer Pattern
│   │   └── resources/
│   │       └── application.properties                   # Configuration
├── DATABASE_SCHEMA.sql                                  # MySQL Schema
├── pom.xml                                              # Maven Dependencies
└── README.md                                            # This file

```

---

## SOLID Principles Implementation

### **1. Single Responsibility Principle (SRP)**

Each service handles ONE responsibility:

- `OrderService` → Only manages order operations
- `PaymentService` → Only processes payments
- `NotificationService` → Only sends notifications
- `WalletService` → Only manages wallet transactions

**Example:**
```java
@Service
public class WalletService {
    // ONLY handles wallet-related operations
    public void addToWallet(User user, BigDecimal amount, String description)
    public boolean deductFromWallet(User user, BigDecimal amount, String description)
}
```

### **2. Open/Closed Principle (OCP)**

System is **open for extension**, **closed for modification**:

- **Discount Strategies** can be added without modifying existing code
- **Payment Methods** can be extended via new Strategy classes
- **User Types** can be added via UserFactory without changing existing logic

**Example:**
```java
public enum DiscountType {
    PERCENTAGE, FIXED_AMOUNT, FIRST_ORDER, SEASONAL
}
// New discount types can be added and handled in switch statement
```

### **3. Liskov Substitution Principle (LSP)**

Subtypes are substitutable for their base types:

- Both `Customer` and `PremiumCustomer` can be treated as `User`
- All payment strategies implement `PaymentStrategy` uniformly
- All discount types use the same interface

### **4. Interface Segregation Principle (ISP)**

Clients not forced to depend on unused methods:

- `PaymentStrategy` interface has only payment-related methods
- `OrderObserver` interface defines only observer operations

### **5. Dependency Inversion Principle (DIP)**

Depend on abstractions, not concretions:

- Controllers depend on **Service interfaces**, not implementations
- Services depend on **Repository interfaces**, not database implementations
- Payment processing depends on **PaymentStrategy interface**, not concrete implementations

---

## Design Patterns Used

### **1. Factory Method Pattern (Creational)**

**File:** `com.quickcommerce.patterns.factory.UserFactory.java`

**Purpose:** Create different types of users without specifying exact classes

**Implementation:**
```java
User customer = UserFactory.createUser(
    UserRole.CUSTOMER, 
    email, password, name, phone
);

User deliveryPartner = UserFactory.createUser(
    UserRole.DELIVERY_PARTNER,
    email, password, name, phone
);
```

**SOLID Alignment:** OCP - New user types can be added without modifying calling code

---

### **2. Proxy Pattern (Structural)**

**File:** `com.quickcommerce.patterns.proxy/`

**Purpose:** Lazy loading of high-resolution product images

**Implementation:**
```java
Image lazyImage = new ProxyImage("product-image.jpg", 1024000);
lazyImage.display(); // Loads image ONLY when accessed
```

**Benefit:** Improves performance by deferring expensive image loading

---

### **3. Strategy Pattern (Behavioral)**

**File:** `com.quickcommerce.patterns.strategy/`

**Purpose:** Allow runtime switching of payment methods

**Implementation:**
```java
PaymentStrategy strategy;
if (paymentMethod == PaymentMethod.UPI) {
    strategy = new UPIPaymentStrategy(upiId, pin);
} else if (paymentMethod == PaymentMethod.CARD) {
    strategy = new CardPaymentStrategy(cardNumber, ...);
}
strategy.processPayment(order, amount);
```

**Supported Methods:**
- UPI Payment
- Card Payment (Debit/Credit)
- Cash on Delivery (COD)
- Wallet Payment

**SOLID Alignment:** DIP - Controllers depend on `PaymentStrategy` interface

---

### **4. Observer Pattern (Behavioral)**

**File:** `com.quickcommerce.patterns.observer/`

**Purpose:** Notify multiple apps when order status changes

**Implementation:**
```java
OrderSubject subject = new OrderSubject();
subject.attachObserver(new CustomerAppObserver());
subject.attachObserver(new DeliveryPartnerAppObserver());

// When order status changes:
subject.notifyObservers(order, oldStatus, newStatus);
// All attached observers are automatically notified
```

**Observers:**
- Customer App (sends push notifications to customer)
- Delivery Partner App (notifies delivery partner)

---

## Database Schema

### **Key Tables**

1. **users** - User profiles with roles
2. **products** - Product catalog with inventory
3. **carts** & **cart_items** - Shopping cart
4. **orders** & **order_items** - Order management
5. **deliveries** - Delivery assignments and tracking
6. **order_tracking** - Order status history
7. **discounts** - Discount codes and offers
8. **wallet_transactions** - Wallet transactions

### **To Set Up Database:**

```bash
mysql -u root -p < DATABASE_SCHEMA.sql
```

---

## API Endpoints

### **Module 4: User & Authentication**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user |
| POST | `/api/users/login` | User login (JWT) |
| GET | `/api/users/{userId}` | Get user profile |
| GET | `/api/users/delivery-partners` | Get all delivery partners |
| PUT | `/api/users/{userId}` | Update user profile |

### **Module 1: Inventory & Catalog**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products/add` | Add new product |
| GET | `/api/products/{productId}` | Get product by ID |
| GET | `/api/products/search?keyword=` | Search products |
| GET | `/api/products/category/{category}` | Get by category |
| GET | `/api/products/available` | Get available products |
| PUT | `/api/products/{productId}/stock` | Update stock |
| GET | `/api/products/admin/low-stock` | Low stock products |

### **Module 2: Cart & Orders**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart/{userId}` | Get user cart |
| POST | `/api/cart/{userId}/add/{productId}` | Add to cart |
| DELETE | `/api/cart/{userId}/remove/{productId}` | Remove from cart |
| PUT | `/api/cart/{userId}/update/{productId}` | Update quantity |
| POST | `/api/orders/create` | Create order |
| GET | `/api/orders/{orderId}` | Get order details |
| GET | `/api/orders/history/{userId}` | Order history |
| GET | `/api/orders/{orderId}/tracking` | Order tracking |

### **Module 3: Delivery & Tracking**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/delivery/assign` | Assign delivery partner |
| PUT | `/api/delivery/{deliveryId}/status` | Update status |
| PUT | `/api/delivery/{deliveryId}/location` | Update location |
| GET | `/api/delivery/partner/{partnerId}` | Partner deliveries |
| GET | `/api/delivery/active` | Active deliveries |

### **Module 4: Wallet**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/wallet/{userId}/add` | Add to wallet |
| GET | `/api/wallet/{userId}/balance` | Check balance |
| GET | `/api/wallet/{userId}/transactions` | Transaction history |

---

## Running the Application

### **Prerequisites**
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### **Setup Steps**

1. **Clone the project**
```bash
cd /Users/kanakgoyal/Desktop/ooad\ mini\ project
```

2. **Create database**
```bash
mysql -u root -p < DATABASE_SCHEMA.sql
```

3. **Configure application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quick_commerce
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

4. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access application**
- API Base URL: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Key Features by Module

### **Module 1: INVENTORY & CATALOG (Member 1)**

**Major Feature: Stock Management**
- Real-time stock updates
- Out-of-stock logic with automatic availability toggle
- Low stock alerts
- Stock validation before adding to cart

**Minor Feature: Search & Suggestions**
- Product search by keyword
- Search accepts partial matches
- Category and subcategory filtering

### **Module 2: ORDER & CART (Member 2)**

**Major Feature: Order Processing**
- Cart management with stock validation
- Price calculation with taxes (5%)
- Discount application (Multiple discount types)
- Delivery charge calculation
- Order creation and confirmation

**Minor Feature: Order History**
- Complete order history per customer
- One-click reorder functionality
- Order status tracking

### **Module 3: DELIVERY & TRACKING (Member 3)**

**Major Feature: Dispatch System**
- Automatic delivery partner assignment
- ETA calculation using Haversine formula
- Distance calculation between coordinates
- Estimated delivery time: Distance/Speed + Buffer

**Minor Feature: Live Status Updates**
- Order status transitions (Packing → Out for Delivery → Delivered)
- Real-time location tracking
- Delay detection and notifications
- Observer pattern for instant notifications

### **Module 4: USER & PROFILE (Member 4)**

**Major Feature: Authentication & RBAC**
- Role-based access control (4 roles)
- JWT token-based authentication
- User factory for multi-type user creation
- User verification for delivery partners

**Minor Feature: Wallet & Payments**
- Wallet balance management
- Credit and debit transactions
- Transaction history
- Multiple payment methods support

---

## OOAD Concepts Demonstrated

### **Use Cases Covered**
1. ✅ Register and Login
2. ✅ Browse Products
3. ✅ Search and Filter
4. ✅ Add to Cart
5. ✅ Checkout with Multiple Payments
6. ✅ Place Order
7. ✅ Track Order Status
8. ✅ Assign Delivery Partner
9. ✅ Real-time Delivery Tracking
10. ✅ Wallet Management

### **Actors**
- Customer
- Delivery Partner
- Admin
- Store Manager

### **OOAD Principles**
- Abstraction (Abstract classes, Interfaces)
- Encapsulation (Private fields, Getters/Setters)
- Inheritance (Entity hierarchy)
- Polymorphism (Strategy pattern, Factory pattern)

---

## Testing the APIs

### **Sample API Calls**

**1. Register Customer**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "pass123",
    "fullName": "John Doe",
    "phoneNumber": "9876543210",
    "role": "CUSTOMER",
    "address": "123 Main St",
    "city": "Delhi",
    "zipCode": "110001"
  }'
```

**2. Login**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "pass123"
  }'
```

**3. Search Products**
```bash
curl http://localhost:8080/api/products/search?keyword=milk
```

**4. Create Order**
```bash
curl -X POST http://localhost:8080/api/orders/create?userId=1 \
  -H "Content-Type: application/json" \
  -d '{
    "cartItems": [...],
    "paymentMethod": "UPI",
    "deliveryAddress": "Complete Address",
    "deliveryCity": "Delhi",
    "deliveryZipCode": "110001"
  }'
```

---

## Deliverables for Phase 1

This project includes:

1. ✅ **Complete Source Code** with all 4 modules
2. ✅ **Database Schema** (DATABASE_SCHEMA.sql)
3. ✅ **MVC Architecture** (Model-View-Controller)
4. ✅ **Design Patterns** (4 patterns implemented)
5. ✅ **SOLID Principles** (All 5 principles)
6. ✅ **REST APIs** (Complete CRUD operations)
7. ✅ **Authentication & RBAC** (JWT + Role-based)
8. ✅ **Comprehensive Documentation** (This README)
9. ✅ **Project Structure** (Well-organized)

---

## Future Enhancements

1. Email and SMS notifications
2. Admin dashboard
3. Payment gateway integration (Razorpay, Stripe)
4. Real-time map integration (Google Maps)
5. Advanced analytics and reporting
6. Mobile app (React Native/Flutter)
7. GraphQL API support
8. Microservices architecture

---

## Contributors

- Member 1: Inventory & Catalog
- Member 2: Order & Cart
- Member 3: Delivery & Tracking
- Member 4: User & Profile

---

## License

This project is for educational purposes.

---

## Contact

For questions or clarifications, refer to the code comments and documentation.

**Happy Coding! 🚀**
