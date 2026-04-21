# Quick Commerce Delivery System - OOAD Mini Project

## Project Overview

This project is a Java Spring Boot application for a quick-commerce delivery platform. It is structured as an OOAD mini project and demonstrates MVC architecture, layered design, REST APIs, and multiple design patterns.

The system covers:

- user registration and login
- product browsing and search
- cart and order management
- delivery tracking
- wallet operations

## Tech Stack

- Java 17
- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- H2 database for local development
- MySQL driver included for future or alternate database setup
- Maven

## Current Runtime Behavior

- The application runs locally with an in-memory H2 database by default.
- Database schema is recreated on startup because `spring.jpa.hibernate.ddl-auto=create-drop` is enabled.
- The H2 console is available at `http://localhost:8080/h2-console`.
- All routes are currently permitted by the active security configuration.
- JWT-related code exists, but request authorization is not currently enforced by Spring Security.

## Project Highlights

- Spring Boot MVC architecture
- REST endpoints for all core modules
- Layered structure with controllers, services, repositories, DTOs, and entities
- Design patterns: Factory, Proxy, Strategy, Observer
- Local web UI pages served from static resources
- Sample seed data added at startup

## Team Division

| Member | Module | Major Feature | Minor Feature |
|--------|--------|---------------|---------------|
| Member 1 | Inventory and Catalog | Stock management | Search and suggestions |
| Member 2 | Order and Cart | Order processing | Order history and re-ordering |
| Member 3 | Delivery and Tracking | Dispatch system | Live status updates |
| Member 4 | User and Profile | Authentication and roles | Wallet and payments |

## Project Structure

```text
ooad_mini_projk/
|- src/main/java/com/quickcommerce/
|  |- DeliverySystemApplication.java
|  |- config/
|  |- controller/
|  |- dto/
|  |- entity/
|  |- enums/
|  |- patterns/
|  |- repository/
|  `- service/
|- src/main/resources/
|  |- application.properties
|  |- static/
|  `- templates/
|- DATABASE_SCHEMA.sql
|- pom.xml
|- run-local.bat
`- README.md
```

## Design Patterns Used

### 1. Factory Method

File: `com.quickcommerce.patterns.factory.UserFactory`

Used to create users based on role without exposing creation logic to callers.

### 2. Proxy Pattern

Files: `com.quickcommerce.patterns.proxy.*`

Used to represent lazy image loading behavior.

### 3. Strategy Pattern

Files: `com.quickcommerce.patterns.strategy.*`

Used to support multiple payment methods such as UPI, card, COD, and wallet.

### 4. Observer Pattern

Files: `com.quickcommerce.patterns.observer.*`

Used for order status update notifications.

## Local Setup

### Prerequisites

- Java 17 or newer

You do not need MySQL for the current default local setup.

### Run the application

From the project folder:

```powershell
cd c:\ooad_mini_projkm\ooad_mini_projk
.\run-local.bat
```

Alternative Maven command:

```powershell
mvn spring-boot:run
```

### Access the application

- Home page: `http://localhost:8080/`
- H2 console: `http://localhost:8080/h2-console`

## Web Pages

These routes redirect to the static HTML pages included in the project:

- `/`
- `/register`
- `/login`
- `/products`
- `/cart`
- `/orders`
- `/deliveries`

## API Endpoints

Note: the app does not use an `/api` prefix right now. Routes begin directly with `/users`, `/products`, `/cart`, `/orders`, `/delivery`, and `/wallet`.

### User and Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users/register` | Register a new user |
| POST | `/users/login` | Login and receive JWT-related response data |
| GET | `/users/{userId}` | Get user profile |
| PUT | `/users/{userId}` | Update user profile |
| GET | `/users/delivery-partners` | List delivery partners |
| GET | `/users/admin/all-users` | List all users |
| POST | `/users/verify-delivery-partner/{deliveryPartnerId}` | Verify a delivery partner |

### Product Catalog

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/products/add` | Add a product |
| GET | `/products/{productId}` | Get product by ID |
| GET | `/products/search?keyword=milk` | Search products |
| GET | `/products/category/{category}` | Filter by category |
| GET | `/products/subcategory/{subcategory}` | Filter by subcategory |
| GET | `/products/available` | List available products |
| PUT | `/products/{productId}/stock?newStock=10` | Update stock |
| GET | `/products/admin/low-stock` | List low-stock products |

### Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/cart/{userId}` | Get cart |
| POST | `/cart/{userId}/add/{productId}?quantity=1` | Add item to cart |
| DELETE | `/cart/{userId}/remove/{productId}` | Remove item from cart |
| PUT | `/cart/{userId}/update/{productId}?newQuantity=2` | Update quantity |
| DELETE | `/cart/{userId}/clear` | Clear cart |
| GET | `/cart/{userId}/total` | Get cart total |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/orders/create?userId=1` | Create order from cart |
| GET | `/orders/{orderId}` | Get order details |
| GET | `/orders/history/{userId}` | Get order history |
| GET | `/orders/{orderId}/tracking` | Get tracking history |
| GET | `/orders/admin/pending` | Get pending orders |
| POST | `/orders/reorder/{previousOrderId}?userId=1` | Re-order from a previous order |
| POST | `/orders/{orderId}/pay?paymentMethod=UPI` | Process payment |

### Delivery

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/delivery/assign` | Assign delivery partner |
| PUT | `/delivery/{deliveryId}/status` | Update delivery status |
| PUT | `/delivery/{deliveryId}/location` | Update delivery location |
| GET | `/delivery/partner/{deliveryPartnerId}` | Get deliveries for a partner |
| GET | `/delivery/active` | List active deliveries |

### Wallet

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/wallet/{userId}/add` | Add money to wallet |
| GET | `/wallet/{userId}/balance` | Get wallet balance |
| GET | `/wallet/{userId}/transactions` | Get wallet transactions |

## Sample Requests

### Register a customer

```bash
curl -X POST http://localhost:8080/users/register \
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

### Login

```bash
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "pass123"
  }'
```

### Search products

```bash
curl "http://localhost:8080/products/search?keyword=milk"
```

### Create an order

```bash
curl -X POST "http://localhost:8080/orders/create?userId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "cartId": 1,
    "paymentMethod": "UPI",
    "deliveryAddress": "123 Main St",
    "deliveryCity": "Delhi",
    "deliveryZipCode": "110001",
    "specialInstructions": "Leave at the door"
  }'
```

## Database Notes

- `DATABASE_SCHEMA.sql` is included in the repository.
- The current default app configuration does not load MySQL automatically.
- If you want to switch to MySQL, update `src/main/resources/application.properties` accordingly.

## Security Notes

- Spring Security is present in the project.
- Passwords are encoded with BCrypt.
- JWT service code exists for login/token generation flows.
- The current filter chain permits all requests, so RBAC is not yet enforced at request level.

## Limitations and Future Improvements

- enforce authenticated and role-based access on routes
- add Swagger or OpenAPI documentation if API docs are needed in-browser
- add persistent database configuration profiles
- expand automated tests
- add admin dashboard and reporting
- integrate external payment gateways

## License

This project is for educational purposes.
