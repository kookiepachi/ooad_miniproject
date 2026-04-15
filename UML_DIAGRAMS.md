# UML Diagrams - Quick Commerce Delivery System

## 1. USE CASE DIAGRAM

```
                                    ┌─────────────────┐
                                    │   Quick Commerce│
                                    │  Delivery System│
                                    └─────────────────┘
                                            │
                    ┌───────────────────────┼───────────────────────┐
                    │                       │                       │
                    ▼                       ▼                       ▼
          ┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐
          │  CUSTOMER (Actor)│   │ DELIVERY_PARTNER │   │  ADMIN (Actor)   │
          │                  │   │    (Actor)       │   │                  │
          └──────────────────┘   └──────────────────┘   └──────────────────┘
                    │                       │                       │
                    ├─► Browse Products     │                       │
                    │                       │                       │
                    ├─► Search Products     │                       │
                    │                       │                       │
                    ├─► Add to Cart         │                       │
                    │                       │                       │
                    ├─► Place Order         │                       ├─► Manage Users
                    │                       │                       │
                    ├─► View Order History  │                       ├─► Manage Products
                    │                       │                       │
                    ├─► Track Order         ├─► Accept Delivery     ├─► Verify Delivery Partners
                    │                       │                       │
                    ├─► Rate Delivery       ├─► Update Location     ├─► View Analytics
                    │                       │                       │
                    ├─► Manage Wallet       ├─► Mark Delivered      └────────────────
                    │                       │
                    └──────────────────     └──► Update Order Status
                         Register
                            │
                            ├─► Customer Registration
                            │
                            └─► Delivery Partner Registration
```

---

## 2. CLASS DIAGRAM (Simplified)

```
┌────────────────────────────────────────────────────────────────────────┐
│                          INHERITANCE HIERARCHY                         │
└────────────────────────────────────────────────────────────────────────┘

                              ┌──────────────┐
                              │   PaymentStrategy (Interface)
                              │  ◄interface► │
                              └──────────────┘
                                    ▲
                    ┌───────────────┼───────────────┬─────────────┐
                    │               │               │             │
          ┌─────────────────┐  ┌─────────────────┐ │             │
          │ UPIPayment      │  │ CardPayment     │ │             │
          │ Strategy        │  │ Strategy        │ │             │
          └─────────────────┘  └─────────────────┘ │             │
                                                    │             │
                              ┌─────────────────┐ ┌────────────────────┐
                              │ CODPayment      │ │ WalletPayment      │
                              │ Strategy        │ │ Strategy           │
                              └─────────────────┘ └────────────────────┘


┌────────────────────────────────────────────────────────────────────────┐
│                    ENTITY CLASS DIAGRAM                                 │
└────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐
│        User                 │
├─────────────────────────────┤
│ - user_id: Long             │
│ - email: String             │
│ - password: String          │
│ - fullName: String          │
│ - role: UserRole            │
│ - walletBalance: BigDecimal  │
│ - isVerified: Boolean       │
│ - isActive: Boolean         │
├─────────────────────────────┤
│ + registerUser()            │
│ + loginUser()               │
│ + updateProfile()           │
│ + getRole()                 │
└─────────────────────────────┘
          │
          │ (1:N)
          │
┌─────────────────────────────┐
│       Order                 │
├─────────────────────────────┤
│ - order_id: Long            │
│ - customer: User            │
│ - orderStatus: OrderStatus  │
│ - totalAmount: BigDecimal   │
│ - subtotal: BigDecimal      │
│ - taxAmount: BigDecimal     │
│ - discountAmount: BigDecimal│
│ - paymentMethod: PaymentMethod
│ - createdAt: LocalDateTime  │
├─────────────────────────────┤
│ + createOrder()             │
│ + calculateTotal()          │
│ + updateStatus()            │
│ + applyDiscount()           │
└─────────────────────────────┘
          │
          │ (1:N)
          │
┌─────────────────────────────┐
│     OrderItem               │
├─────────────────────────────┤
│ - order_item_id: Long       │
│ - product: Product          │
│ - quantity: Integer         │
│ - priceAtTime: BigDecimal   │
│ - totalPrice: BigDecimal    │
├─────────────────────────────┤
│ + calculateTotal()          │
└─────────────────────────────┘
                              │
                              │ (references)
                              │
                    ┌─────────────────────────────┐
                    │      Product                │
                    ├─────────────────────────────┤
                    │ - product_id: Long          │
                    │ - productName: String       │
                    │ - price: BigDecimal         │
                    │ - stock: Integer            │
                    │ - category: String          │
                    │ - isAvailable: Boolean      │
                    │ - rating: Double            │
                    ├─────────────────────────────┤
                    │ + decreaseStock()           │
                    │ + increaseStock()           │
                    │ + isOutOfStock()            │
                    │ + searchByKeyword()         │
                    └─────────────────────────────┘


┌─────────────────────────────┐
│      Delivery               │
├─────────────────────────────┤
│ - delivery_id: Long         │
│ - order: Order              │
│ - deliveryPartner: User     │
│ - pickupLatitude: Double    │
│ - deliveryLatitude: Double  │
│ - currentLatitude: Double   │
│ - currentLongitude: Double  │
│ - estimatedTime: Long       │
│ - estimatedDistance: Long   │
│ - status: OrderStatus       │
├─────────────────────────────┤
│ + assignPartner()           │
│ + calculateETA()            │
│ + updateLocation()          │
│ + updateStatus()            │
└─────────────────────────────┘


┌─────────────────────────────┐
│      Cart                   │
├─────────────────────────────┤
│ - cart_id: Long             │
│ - user: User                │
│ - cartItems: List           │
│ - totalPrice: BigDecimal    │
├─────────────────────────────┤
│ + addItem()                 │
│ + removeItem()              │
│ + updateQuantity()          │
│ + calculateTotal()          │
│ + clearCart()               │
└─────────────────────────────┘


┌─────────────────────────────┐
│      CartItem               │
├─────────────────────────────┤
│ - cartItem_id: Long         │
│ - product: Product          │
│ - quantity: Integer         │
│ - totalPrice: BigDecimal    │
├─────────────────────────────┤
│ + updatePrice()             │
└─────────────────────────────┘


┌─────────────────────────────┐
│      Discount               │
├─────────────────────────────┤
│ - discount_id: Long         │
│ - discountCode: String      │
│ - discountType: DiscountType│
│ - discountValue: BigDecimal │
│ - isValid: Boolean          │
├─────────────────────────────┤
│ + validateDiscount()        │
│ + calculateDiscount()       │
│ + isValid()                 │
└─────────────────────────────┘
```

---

## 3. SEQUENCE DIAGRAM - ORDER PLACEMENT FLOW

```
Customer    CartService    OrderService    PaymentService    OrderRepository
   │              │              │                │                │
   │─ Add Item ──▶│              │                │                │
   │              │─ Validate ──▶│                │                │
   │              │◀─ Stock OK ──│                │                │
   │              │─ Add Item ──▶│                │                │
   │◀─ Item Added │              │                │                │
   │              │              │                │                │
   │─ Checkout ──▶│              │                │                │
   │              │─ Create Order─▶               │                │
   │              │              │─ Process────▶ │                │
   │              │              │  Payment      │                │
   │              │              │◀──Payment OK──│                │
   │              │              │─ Save────────────────────────▶ │
   │              │              │  Order                          │
   │              │              │              │       ◀─ Saved ─│
   │              │◀─ Clear ─────│              │                │
   │              │  Cart        │              │                │
   │◀─ Order ─────│              │              │                │
   │  Confirmed   │              │              │                │
```

---

## 4. SEQUENCE DIAGRAM - DELIVERY TRACKING FLOW

```
Customer App    OrderService    DeliveryService    DeliveryPartner App
     │                │                 │                    │
     │   Place Order  │                 │                    │
     ├────────────────▶                 │                    │
     │                │   Assign────────▶                    │
     │                │   Partner       │                    │
     │                │                 │   Notify──────────▶
     │                │                 │  (Order Assigned)  │
     │                │   Update Status │                    │
     │   Get Status   │   to PACKING    │                    │
     ├────────────────▶                 │                    │
     │◀────────────────   Notify       │                    │
     │  PACKING        Observer         │                    │
     │                                  │                    │
     │                │   Update Status │                    │
     │                ├──────────────────▶   Out for Delivery
     │   Get Status   │                 │                    │
     ├────────────────▶                 │   Update Loc───────▶
     │◀────────────────               │                    │
     │  OUT_FOR_DELIVERY  │                    │
     │                                  │   Mark────────────▶
     │   Get Status   │   Update Status │  Delivered        │
     ├────────────────▶   to DELIVERED  │                    │
     │◀────────────────                 │                    │
     │  DELIVERED       Notify─────────▶                    │
     │                 Observer        │                    │
```

---

## 5. STATE DIAGRAM - ORDER STATUS

```
              ┌─────────────────────────────────────────┐
              │         ORDER STATES                    │
              └─────────────────────────────────────────┘

                    ┌──[START]──┐
                    │           │
                    ▼           │
            ┌──────────────┐    │
            │   PENDING    │    │
            └──────────────┘    │
                    │           │
     (Payment OK)   │           │ (Cancelled)
                    ▼           ▼
            ┌──────────────┐    ┌──────────────┐
            │  CONFIRMED   │    │  CANCELLED   │
            └──────────────┘    └──────────────┘
                    │
     (Order Ready)  │
                    ▼
            ┌──────────────┐
            │   PACKING    │
            └──────────────┘
                    │
     (Packed)       │
                    ▼
            ┌──────────────┐
            │    PACKED    │
            └──────────────┘
                    │
    (Partner Pickup)│
                    ▼
            ┌──────────────────┐
            │ OUT_FOR_DELIVERY │
            └──────────────────┘
                    │
    (Delivered)     │
                    ▼
            ┌──────────────┐
            │  DELIVERED   │
            └──────────────┘
                    │
                    ▼
                  [END]

SPECIAL TRANSITIONS:
- PENDING ──▶ CANCELLED (Anytime before confirmation)
- Any State ──▶ RETURNED (After delivery with issues)
```

---

## 6. DESIGN PATTERN DIAGRAMS

### 6.1 Factory Method Pattern

```
                    ┌──────────────────┐
                    │  UserFactory     │
                    │ (static methods) │
                    └──────────────────┘
                            │
                            │ createUser(role)
                            │
                ┌───────────┼───────────┐
                │           │           │
                ▼           ▼           ▼
           ┌────────┐  ┌────────┐  ┌────────────┐
           │Customer│  │ Admin  │  │DeliveryPtnr│
           └────────┘  └────────┘  └────────────┘
                This allows creating different User types
                without specifying the exact class
```

### 6.2 Strategy Pattern

```
Client Code (Checkout)
        │
        │ PaymentMethod
        │
        ▼
┌─────────────────────────┐
│  PaymentService         │
│ (uses Strategy)         │
└─────────────────────────┘
        │
        │ depends on
        ▼
┌─────────────────────────┐
│ PaymentStrategy         │
│ (Interface)             │
├─────────────────────────┤
│ + processPayment()      │
│ + validateDetails()     │
│ + getMethodName()       │
└─────────────────────────┘
        ▲
        │ implements
        │
    ┌───┴───┬───────────┬─────────┐
    │       │           │         │
    ▼       ▼           ▼         ▼
┌──────┐ ┌──────┐ ┌───────┐ ┌──────────┐
│ UPI  │ │Card  │ │ COD   │ │ Wallet   │
└──────┘ └──────┘ └───────┘ └──────────┘

Runtime Strategy Selection:
if paymentMethod == UPI
    strategy = new UPIPaymentStrategy()
else if paymentMethod == CARD  
    strategy = new CardPaymentStrategy()
```

### 6.3 Proxy Pattern

```
Client (ProductService)
        │
        │ image.display()
        │
        ▼
┌──────────────┐
│ ProxyImage   │ (Lazy Loading)
│              │
│ - imageUrl   │
│ - realImage  │ (null initially)
└──────────────┘
        │
        │ On first display()
        │ Creates RealImage
        │
        ▼
┌──────────────┐
│ RealImage    │
│              │
│ - imageUrl   │
│ - imageData  │ (loaded now)
└──────────────┘

Benefit: Image is loaded only when actually displayed
        (lazy loading improves performance)
```

### 6.4 Observer Pattern

```
              ┌─────────────────┐
              │  Order Status   │
              │    Changes      │
              └─────────────────┘
                       │
                       │ notifyObservers()
                       │
         ┌─────────────┴──────────────┐
         │                            │
         ▼                            ▼
┌──────────────────────┐   ┌──────────────────────┐
│ CustomerAppObserver  │   │DeliveryPartnerObs.   │
│                      │   │                      │
│ + update()           │   │ + update()           │
│   ├─ Send SMS        │   │   ├─ Send Alert      │
│   ├─ Update UI       │   │   ├─ Update Map      │
│   └─ Log info        │   │   └─ Mark Ready      │
└──────────────────────┘   └──────────────────────┘

Central Manager (OrderSubject):
┌─────────────────────────────────┐
│    OrderSubject                 │
├─────────────────────────────────┤
│ - observers: List<OrderObserver>│
├─────────────────────────────────┤
│ + attachObserver()              │
│ + detachObserver()              │
│ + notifyObservers()             │
└─────────────────────────────────┘
        │
        │ manages
        │
    ┌───┴──────────┬────────────┐
    │              │            │
   ...          ... (multiple observers)
```

---

## 7. COMPONENT DIAGRAM

```
┌───────────────────────────────────────────────────────────┐
│                  Spring Boot Application                   │
├───────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │            REST API Layer (Controllers)             │  │
│  │  [UserCtrl] [ProductCtrl] [OrderCtrl] [DeliveryCtrl]│  │
│  └─────────────────────────────────────────────────────┘  │
│                     │   │   │   │                          │
│                     ▼   ▼   ▼   ▼                          │
│  ┌─────────────────────────────────────────────────────┐  │
│  │        Business Logic Layer (Services)              │  │
│  │  [UserService] [ProductService] [OrderService]     │  │
│  │  [CartService] [DeliveryService] [PaymentService]  │  │
│  │  [DiscountService] [WalletService] [JwtService]    │  │
│  └─────────────────────────────────────────────────────┘  │
│                     │   │   │   │                          │
│                     ▼   ▼   ▼   ▼                          │
│  ┌─────────────────────────────────────────────────────┐  │
│  │      Data Access Layer (Repositories)               │  │
│  │  [UserRepository] [ProductRepository]               │  │
│  │  [OrderRepository] [DeliveryRepository]             │  │
│  └─────────────────────────────────────────────────────┘  │
│                     │                                      │
│                     ▼                                      │
│  ┌─────────────────────────────────────────────────────┐  │
│  │        Database Layer (MySQL)                       │  │
│  │  [users] [products] [orders] [deliveries] [....]    │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │          Design Patterns & Enums                    │  │
│  │  [Factory] [Strategy] [Proxy] [Observer] [Enums]   │  │
│  │  [DTOs]                                             │  │
│  └─────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────┘
```

---

## 8. MODULE INTERACTION DIAGRAM

```
┌─────────────────────────────────────────────────────────────┐
│           MODULE INTERACTIONS                               │
└─────────────────────────────────────────────────────────────┘

MODULE 1: INVENTORY & CATALOG
    ├─ ProductService
    ├─ ProductRepository
    ├─ ProductController
    └─ (Design Pattern: Proxy - Lazy Image Loading)
             │
             │ Used By
             ▼
         MODULE 2
      
MODULE 4: USER & AUTHENTICATION
    ├─ UserService
    ├─ JwtService
    ├─ UserRepository
    ├─ UserController
    ├─ WalletService
    ├─ (Design Pattern: Factory - User Creation)
    └─ (Design Pattern: Strategy - Payment Methods)
             │
             │ Creates Users & Authenticates
             ▼
         Used by All Modules
      
MODULE 2: CART & ORDERS
    ├─ CartService
    ├─ OrderService
    ├─ DiscountService
    ├─ PaymentService
    ├─ CartRepository
    ├─ OrderRepository
    ├─ OrderController
    ├─ (Design Pattern: Strategy - Discount Calculations)
    └─ (Design Pattern: Strategy - Payment Processing)
             │
             │ Uses Module 1 (Products)
             │ Uses Module 4 (Auth, Wallet)
             │ Notifies Module 3 (Delivery)
             ▼
    ┌────────────────────┐
    │ Triggers Creation  │
    │    of Delivery     │
    └────────────────────┘
             │
             ▼
MODULE 3: DELIVERY & TRACKING
    ├─ DeliveryService
    ├─ DeliveryRepository
    ├─ DeliveryController
    ├─ OrderTrackingRepository
    ├─ (Design Pattern: Observer - Status Notifications)
    └─ (Calculates: ETA, Distance, Delivery Assignment)
             │
             │ Notifies
             ▼
    ┌────────────────────────────────┐
    │ Observer Pattern Implementation│
    │ ├─ CustomerAppObserver         │
    │ └─ DeliveryPartnerAppObserver  │
    └────────────────────────────────┘

FLOW EXAMPLE - Place Order:
Customer Registers (Module 4)
    ↓
Browse/Search Products (Module 1)
    ↓
Add to Cart (Module 2)
    ↓
Checkout with Discounts & Payment (Module 2 + Module 4)
    ↓
Order Created & Delivery Assigned (Module 3)
    ↓
Status Updates & Live Tracking (Module 3 Observer Pattern)
    ↓
Order Delivered & Wallet Updated (Module 4)
```

---

## 9. DATA FLOW DIAGRAM

```
         ┌─────────────────────────────────┐
         │      Client Application        │
         │    (Web/Mobile)                 │
         └─────────────────────────────────┘
                    │
                    │ HTTP Requests
                    ▼
         ┌─────────────────────────────────┐
         │   REST API Gateway              │
         │   (Spring Boot Controller)       │
         └─────────────────────────────────┘
                    │
         ┌──────────┴──────────┐
         │                     │
         ▼                     ▼
    ┌─────────────┐    ┌─────────────┐
    │  Service    │    │  Service    │
    │   Layer     │    │   Layer     │
    └─────────────┘    └─────────────┘
         │                     │
         └──────────┬──────────┘
                    │
                    ▼
      ┌──────────────────────────┐
      │  Repository Layer        │
      │  (Data Access Object)    │
      └──────────────────────────┘
                    │
                    ▼
      ┌──────────────────────────┐
      │   Hibernate (ORM)        │
      │   Spring Data JPA        │
      └──────────────────────────┘
                    │
      ┌─────────────┴──────────────┐
      │                            │
      ▼                            ▼
 ┌─────────────┐           ┌──────────────┐
 │   MySQL     │           │   Cache      │
 │  Database   │           │  (Future)    │
 └─────────────┘           └──────────────┘
```

---

## 10. ERROR HANDLING FLOW

```
Client Request
    │
    ▼
Controller (Receives Request)
    │
    ├─ Validate Input
    │   │
    │   ├─ Valid ──────────────────┐
    │   │                          │
    │   └─ Invalid ────────────────┼────▶ 400 Bad Request
    │                              │
    ▼                              │
Service Layer (Business Logic)     │
    │                              │
    ├─ Execute Operation           │
    │   │                          │
    │   ├─ Success ─────────────┐  │
    │   │                       │  │
    │   └─ Exception ───────────┼──┼────▶ 500 Internal Server Error
    │                           │  │
    ▼                           │  │
Repository Layer         OK Response
(Database)                      │
                                ▼
                        ┌──────────────┐
                        │ 200 OK       │
                        │ 201 Created  │
                        │ 204 No Content
                        └──────────────┘
```

---

**Note:** These UML diagrams are essential documentation for understanding the system architecture and should be included in the Phase 1 submission.

**Last Updated:** January 2026
**Version:** 1.0
