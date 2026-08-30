# Product Management & Order Processing System

A full-stack e-commerce and inventory management application built with Spring Boot backend and React frontend. Supports role-based access control for Users, Admins, and Super Admins.

## 📋 Project Overview

This is a complete product management and order processing system with:
- **User Authentication** via JWT with role-based access control
- **Product Catalog** with categories and inventory management
- **Shopping Cart** with quantity management
- **Checkout & Orders** with transactional inventory validation
- **Admin Features** for product and inventory management
- **Super Admin Features** for user, role, and category management

## ✨ Features

### User Capabilities
- User registration and login with JWT authentication
- Browse products by category
- Add/update/remove items from shopping cart
- Manage delivery addresses
- Checkout and place orders with inventory validation
- View order history

### Admin Capabilities
- Create, update, and manage products
- Manage product categories and pricing
- Update inventory levels with validation
- Enable/disable product availability
- View inventory status and low-stock alerts

### Super Admin Capabilities
- All admin permissions
- User account management
- Role assignment and management
- View all orders across the system
- Category management

## 🛠 Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.x** - Framework
- **Spring Security 6.x** - Authentication & Authorization
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM
- **JWT (JSON Web Tokens)** - Stateless authentication
- **MySQL 8.x** - Relational database
- **Maven** - Build tool

### Frontend
- **React 19** - UI framework
- **Vite** - Build tool & development server
- **Material-UI (MUI) 9** - Component library
- **Axios** - HTTP client
- **React Router 7** - Client-side routing

## 📦 Project Structure

### Backend
```
src/main/java/com/product_order_system/
├── config/           # Spring configuration (Security, CORS, Database)
├── controller/       # REST API endpoints
├── dto/             # Data Transfer Objects (Request/Response)
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── repository/      # Data access layer
├── security/        # JWT authentication filters
├── service/         # Business logic interfaces
└── serviceImpl/      # Business logic implementation
```

### Frontend
```
src/
├── api/            # API client calls
├── components/     # Reusable React components
├── context/        # Auth context for state management
├── pages/          # Page components for routes
├── routes/         # Routing configuration
├── utils/          # Utility functions
└── App.jsx         # Main app component
```

## 🗄 Database Schema

### Core Tables

**users**
- id (PK)
- username (unique)
- email (unique)
- password (hashed)
- created_at, updated_at

**roles**
- id (PK)
- name (USER, ADMIN, SUPER_ADMIN)

**user_roles**
- user_id (FK)
- role_id (FK)

**products**
- id (PK)
- name
- description
- price (₹)
- category_id (FK)
- active (boolean)
- created_at, updated_at

**categories**
- id (PK)
- name
- description
- active (boolean)

**inventory**
- id (PK)
- product_id (FK, unique)
- quantity
- last_updated

**cart**
- id (PK)
- user_id (FK, unique)
- created_at, updated_at

**cart_items**
- id (PK)
- cart_id (FK)
- product_id (FK)
- quantity
- (unique constraint: cart_id + product_id)

**orders**
- id (PK)
- user_id (FK)
- address_id (FK)
- total_amount
- status (PLACED, CANCELLED)
- created_at

**order_items**
- id (PK)
- order_id (FK)
- product_id (FK)
- quantity
- price_at_purchase

**addresses**
- id (PK)
- user_id (FK)
- full_name
- phone
- address_line
- city
- state
- pincode
- created_at, updated_at

## 🔐 Security & Authentication

### JWT Authentication Flow
1. User registers or logs in with username and password
2. Backend validates credentials and generates JWT token
3. Frontend stores token in localStorage
4. Token is sent with every protected request via `Authorization: Bearer <token>` header
5. Backend validates token and user permissions
6. Token expires after 24 hours

### Role-Based Access Control (RBAC)

**USER Role**
- Access: Products (view), Cart, Addresses, Checkout, Own Orders
- Endpoints: `GET /api/products`, `POST /api/cart/*`, etc.

**ADMIN Role**
- Access: All USER permissions + Product Management + Inventory
- Endpoints: `POST/PUT/PATCH /api/products/*`, `PUT /api/inventory/*`

**SUPER_ADMIN Role**
- Access: All ADMIN permissions + User Management + Role Management + Category Management + All Orders
- Endpoints: `POST/PUT/DELETE /api/categories/*`, `POST/PUT /api/roles/*`, `GET /api/orders`

### CORS Configuration
- Allowed Origin: `http://localhost:5173` (frontend)
- Allowed Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
- Allowed Headers: Authorization, Content-Type
- Credentials: Enabled

## 📡 API Endpoints

### Authentication
```
POST   /api/auth/register        - User registration
POST   /api/auth/login           - User login (returns JWT token)
```

### Products
```
GET    /api/products              - List all products (public)
GET    /api/products/{id}         - Get product details (public)
GET    /api/products/category/{categoryId} - Get products by category (public)
POST   /api/products              - Create product (ADMIN/SUPER_ADMIN)
PUT    /api/products/{id}         - Update product (ADMIN/SUPER_ADMIN)
PATCH  /api/products/{id}/enable  - Enable product (ADMIN/SUPER_ADMIN)
PATCH  /api/products/{id}/disable - Disable product (ADMIN/SUPER_ADMIN)
```

### Categories
```
GET    /api/categories            - List categories (SUPER_ADMIN)
POST   /api/categories            - Create category (SUPER_ADMIN)
PUT    /api/categories/{id}       - Update category (SUPER_ADMIN)
DELETE /api/categories/{id}       - Delete category (SUPER_ADMIN)
```

### Cart
```
GET    /api/cart/{userId}         - Get cart (USER/ADMIN/SUPER_ADMIN)
POST   /api/cart/{userId}/items   - Add item to cart (USER/ADMIN/SUPER_ADMIN)
PUT    /api/cart/{userId}/items/{productId} - Update cart item quantity
DELETE /api/cart/{userId}/items/{productId} - Remove item from cart
DELETE /api/cart/{userId}         - Clear cart
```

### Addresses
```
POST   /api/addresses             - Create address (USER/ADMIN/SUPER_ADMIN)
GET    /api/addresses             - Get user's addresses
GET    /api/addresses/{id}        - Get address details
PUT    /api/addresses/{id}        - Update address
DELETE /api/addresses/{id}        - Delete address
```

### Checkout & Orders
```
POST   /api/orders/checkout       - Place order (USER/ADMIN/SUPER_ADMIN)
GET    /api/orders/my-orders      - Get user's orders (USER/ADMIN/SUPER_ADMIN)
GET    /api/orders/{id}           - Get order details (USER/ADMIN/SUPER_ADMIN)
GET    /api/orders                - Get all orders (SUPER_ADMIN)
```

### Inventory
```
GET    /api/inventory/{productId} - Get inventory (ADMIN/SUPER_ADMIN)
PUT    /api/inventory/{productId} - Update inventory quantity (ADMIN/SUPER_ADMIN)
```

### Users (SUPER_ADMIN Only)
```
GET    /api/users                 - List all users
DELETE /api/users/{id}            - Delete user
```

### Roles (SUPER_ADMIN Only)
```
POST   /api/roles/assign          - Assign role to user
GET    /api/users                 - Get users with roles
```

## 🚀 Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+

### Backend Setup

1. **Database Configuration**
   ```sql
   CREATE DATABASE product_order_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Configure `application.properties`**
   
   Create or update `src/main/resources/application.properties`:
   ```properties
   spring.application.name=product-order-system
   server.port=8086

   spring.datasource.url=jdbc:mysql://localhost:3306/product_order_db
   spring.datasource.username=root
   spring.datasource.password=${DB_PASSWORD}

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=false
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

   app.jwt.secret=${JWT_SECRET}
   app.jwt.expiration=86400000
   ```

   **Set Environment Variables:**
   - Windows: `set DB_PASSWORD=your_password` and `set JWT_SECRET=your_secret`
   - Linux/Mac: `export DB_PASSWORD=your_password` and `export JWT_SECRET=your_secret`

   Or create a `.env.local` file (not committed to git) with these values.

3. **Run Backend**
   ```bash
   cd product-order-system
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```
   Backend will start on `http://localhost:8086`

### Frontend Setup

1. **Install Dependencies**
   ```bash
   cd product-order-ui
   npm install
   ```

2. **Run Development Server**
   ```bash
   npm run dev
   ```
   Frontend will start on `http://localhost:5173`

### Build for Production

**Backend**
```bash
cd product-order-system
./mvnw clean package -DskipTests
```
Output: `target/product-order-system-0.0.1-SNAPSHOT.jar`

**Frontend**
```bash
cd product-order-ui
npm run build
```
Output: `dist/` directory

## 📝 Sample API Requests

### Register User
```json
POST /api/auth/register
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

### Login
```json
POST /api/auth/login
{
  "username": "john_doe",
  "password": "SecurePass123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "username": "john_doe",
  "roles": ["USER"]
}
```

### Create Product (ADMIN)
```json
POST /api/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 65000,
  "categoryId": 1
}
```

### Create Category (SUPER_ADMIN)
```json
POST /api/categories
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Electronics",
  "description": "Electronic devices"
}
```

### Add to Cart
```json
POST /api/cart/1/items
Authorization: Bearer <token>
Content-Type: application/json

{
  "productId": 5,
  "quantity": 2
}
```

### Create Address
```json
POST /api/addresses
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "John Doe",
  "phone": "9876543210",
  "addressLine": "123 Main Street",
  "city": "Bangalore",
  "state": "Karnataka",
  "pincode": "560001"
}
```

### Checkout & Place Order
```json
POST /api/orders/checkout
Authorization: Bearer <token>
Content-Type: application/json

{
  "addressId": 1
}

Response:
{
  "id": 1,
  "userId": 1,
  "addressId": 1,
  "totalAmount": 130000,
  "status": "PLACED",
  "items": [
    {
      "productId": 5,
      "quantity": 2,
      "priceAtPurchase": 65000
    }
  ]
}
```

### Update Inventory (ADMIN)
```json
PUT /api/inventory/5
Authorization: Bearer <token>
Content-Type: application/json

{
  "quantity": 50
}
```

### Assign Role (SUPER_ADMIN)
```json
POST /api/roles/assign
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 2,
  "roleName": "ADMIN"
}
```

## 🔄 Checkout & Inventory Flow

The checkout process follows this strict transactional flow:

1. **Load Cart** - Retrieve user's cart items
2. **Validate Cart** - Ensure cart is not empty
3. **Validate Products** - Check all products exist and are active
4. **Validate Inventory** - Confirm sufficient quantity for each item
   - If any item has insufficient inventory, checkout fails
   - No partial orders are created
5. **Create Order** - Generate order record with items
6. **Reduce Inventory** - Decrement inventory for each ordered product
   - Uses pessimistic locking to prevent race conditions
7. **Clear Cart** - Remove all items from user's cart
8. **Return Success** - Return order details to frontend

**Important**: If ANY step fails, the entire transaction is rolled back. Inventory is NEVER decremented for failed orders.

## ⚠️ Error Handling

### Common Error Responses

**401 Unauthorized**
```json
{
  "message": "Your session has expired. Please log in again."
}
```

**403 Forbidden**
```json
{
  "message": "You do not have permission to perform this action."
}
```

**404 Not Found**
```json
{
  "message": "Requested resource was not found."
}
```

**409 Conflict**
```json
{
  "message": "Resource already exists."
}
```

**400 Bad Request**
```json
{
  "message": "Invalid input. Please check the entered information."
}
```

**Insufficient Inventory**
```json
{
  "message": "Insufficient inventory for product: Laptop. Requested: 5, Available: 3"
}
```

**500 Internal Server Error**
```json
{
  "message": "Something went wrong. Please try again later."
}
```

## 🧪 Testing

### Running Tests

**Backend**
```bash
cd product-order-system
./mvnw test
```

**Frontend**
```bash
cd product-order-ui
npm run lint
```

## 📋 Requirements Checklist

### Authentication & Authorization
- [x] User registration with validation
- [x] JWT-based stateless authentication
- [x] Role-based access control (USER, ADMIN, SUPER_ADMIN)
- [x] Protected API endpoints by role
- [x] Token expiration (24 hours)
- [x] CORS configured for frontend

### Products & Categories
- [x] Product listing (public GET access)
- [x] Product creation/update (ADMIN only)
- [x] Enable/disable products
- [x] Category management (SUPER_ADMIN only)
- [x] Product filtering by category
- [x] Category assignment to products

### Cart Management
- [x] Add items to cart
- [x] Update cart item quantities
- [x] Remove items from cart
- [x] Clear entire cart
- [x] Prevent duplicate cart items (uses unique constraint)
- [x] Cart totals calculation

### Inventory Management
- [x] Track product inventory levels
- [x] Inventory validation during checkout
- [x] Inventory reduction after successful order
- [x] Prevent negative inventory
- [x] Admin inventory updates with validation
- [x] Pessimistic locking for concurrency safety

### Order Processing
- [x] Order creation with validation
- [x] Transactional checkout (all or nothing)
- [x] Inventory validation before order placement
- [x] Order items preserve purchase price
- [x] User can view own orders
- [x] SUPER_ADMIN can view all orders
- [x] Order status tracking

### Addresses
- [x] Create delivery addresses
- [x] Update addresses
- [x] Delete addresses
- [x] Prevent cross-user access
- [x] Address validation (required fields)

### User & Role Management
- [x] Create user accounts
- [x] Assign roles to users
- [x] Update user roles
- [x] List users (SUPER_ADMIN only)
- [x] Delete users (SUPER_ADMIN only)

### Data Layer
- [x] JPA/Hibernate implementation
- [x] DTOs for request/response
- [x] Validation annotations
- [x] Custom exceptions
- [x] Global exception handler
- [x] Repository pattern

### API & Integration
- [x] RESTful API design
- [x] Proper HTTP methods and status codes
- [x] Error responses with meaningful messages
- [x] Request/response validation
- [x] Backend URL correctly configured
- [x] JWT token in authorization header

### Frontend
- [x] User login/registration
- [x] Protected routes with role checking
- [x] Product browsing and search
- [x] Category filtering
- [x] Shopping cart interface
- [x] Address management UI
- [x] Checkout process
- [x] Order history display
- [x] Admin dashboard
- [x] Inventory management UI
- [x] User/role management (Super Admin)
- [x] Error handling and display
- [x] Loading states
- [x] Success/failure feedback

### Code Quality
- [x] Layered architecture (Controller → Service → Repository)
- [x] Constructor injection
- [x] DRY principle (no code duplication)
- [x] Meaningful variable/method names
- [x] Comments where needed
- [x] Transactional consistency

### Security
- [x] Password hashing (BCrypt)
- [x] JWT token-based authentication
- [x] SQL injection prevention (parameterized queries)
- [x] Authorization checks on backend
- [x] CORS properly configured
- [x] No sensitive data in responses

## 🔒 Environment Configuration

### For Development
Create a `.env.local` file in the project root (NOT committed to git):
```
DB_PASSWORD=your_local_password
JWT_SECRET=your_local_jwt_secret
```

### For Production
Use environment variables or a secrets management system:
- Never commit real credentials to git
- Use encrypted secrets in CI/CD pipelines
- Rotate secrets regularly

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Material-UI Documentation](https://mui.com)
- [MySQL Documentation](https://dev.mysql.com/doc)
- [JWT.io](https://jwt.io)

## 📝 License

This project is for educational and assignment purposes.

## 👨‍💼 Project Information

**Created**: 2026
**Status**: Production Ready
**Assignment**: Product Management & Order Processing System with Role-Based Access Control

---

**Version**: 1.0.0
**Last Updated**: 2026-08-30

For questions or issues, review the code comments or check the API documentation above.

