# 💰 TransactX - Smart Transaction Management System

A **secure, full-stack, enterprise-grade financial transaction platform** built using **Spring Boot** and **Angular**. It allows users to register, authenticate, and perform multi-currency money transactions while providing a robust Admin portal for user management and advanced transaction tracking.

This project demonstrates real-world backend and frontend concepts like **JWT stateless authentication, Role-Based Access Control (RBAC), multi-currency handling via third-party APIs, high-performance database filtering, and robust REST API design.**

---

## 🚀 Key Features

*   🔐 **Secure User Authentication & RBAC (JWT)**
    *   Stateless authentication using JSON Web Tokens.
    *   Role-Based Access Control distinguishing `USER` and `ADMIN` privileges.
    *   Secure endpoints protected by `@PreAuthorize` and `SecurityConfig`.
*   💸 **Multi-Currency Money Transfer System**
    *   Real-time exchange rate fetching via external API (`open.er-api.com`).
    *   Dynamic currency conversion natively handled on the backend to prevent tampering.
    *   Atomic database transactions using `@Transactional` to ensure financial integrity.
*   📊 **High-Performance Transaction Management**
    *   Store and retrieve detailed transaction records.
    *   **Server-Side Filtering:** Advanced JPQL `@Query` implementations for full-text search (name/email) and Date-Range filtering natively in MySQL.
*   🛡️ **Admin Portal & User Management**
    *   Get all users, view balances, and manage access.
    *   **Safe Deactivation (Soft Delete):** Users are deactivated via an `isActive` flag instead of a hard SQL `DELETE`, preserving financial history and foreign-key integrity.
*   ⚠️ **Robust Exception Handling**
    *   Global Exception Handler using `@RestControllerAdvice` to format errors cleanly (e.g., 403 Forbidden, Validation Errors, Insufficient Balance).

---

## 🛠 Tech Stack

### Backend
*   **Java 17 / Spring Boot 3.x**
*   **Spring Security + JWT**
*   **MySQL** (with `org.hibernate.dialect.MySQL8Dialect`)
*   **JPA / Hibernate**
*   **Maven**
*   **Swagger (OpenAPI)**
*   **RestTemplate** (Third-party API Integration)

### Frontend
*   **Angular 17+** (Standalone Components)
*   **TypeScript / RxJS**
*   **Bootstrap 5 / Custom CSS Glassmorphism**
*   **HTTP Interceptors** for token management

---

## 📂 Project Structure (Backend)

```
src/main/java/transaction/example/transmansys

├── controller     # REST Controllers
├── service        # Business Logic & Third-Party Integrations
├── repository     # Database Access & Custom JPQL Queries
├── entity         # Database Models
├── dto            # Request/Response/Paginated DTOs
├── security       # JWT, Security Filters, & Config
├── config         # Cors & App Configuration
└── exception      # Global Exception Handling
```

---

## 🔑 Authentication Flow

1.  User registers via `/auth/register`
2.  User logs in via `/auth/login`
3.  Backend returns a signed JWT token.
4.  Frontend interceptor automatically attaches token to subsequent request headers:
    ```
    Authorization: Bearer <your_token>
    ```

---

## 📌 API Endpoints

### 🔐 Auth APIs
| Method | Endpoint       | Description   |
| ------ | -------------- | ------------- |
| POST   | /auth/register | Register user |
| POST   | /auth/login    | Login user    |

### 👤 User APIs (Role Protected)
| Method | Endpoint             | Access | Description             |
| ------ | -------------------- | ------ | ----------------------- |
| GET    | /users/me            | ANY    | Get current profile     |
| GET    | /users               | ADMIN  | Get all users           |
| GET    | /users/{id}          | ADMIN  | Get user by ID          |
| PUT    | /users/{id}          | ADMIN  | Update user             |
| DELETE | /users/{id}          | ADMIN  | Deactivate user         |
| PUT    | /users/{id}/activate | ADMIN  | Reactivate user         |

### 💸 Transaction APIs
| Method | Endpoint              | Access | Description                          |
| ------ | --------------------- | ------ | ------------------------------------ |
| POST   | /transaction/transfer | ANY    | Transfer money (supports currencies) |
| GET    | /transaction          | ADMIN  | Get filtered & all transactions      |
| GET    | /transaction/user/{id}| ANY    | Get transactions for specific user   |
| GET    | /transaction/rates    | ANY    | Fetch live currency exchange rates   |

---

## ⚙️ Setup Instructions

### 1. Configure MySQL
Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/transmansys
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 2. Run the Backend
```bash
mvn spring-boot:run
```

### 3. Run the Frontend (Angular)
```bash
npm install
ng serve
```

---

## 🧪 Testing

You can test APIs using:
*   Frontend Web App (http://localhost:4200)
*   Postman
*   Swagger UI

Swagger URL:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧑‍💻 Author
**Ehsan Siddiqui**

---

## ⭐ Resume Highlight
**TransactX:** Designed and implemented a secure, enterprise-grade Transaction Management System using Spring Boot and Angular. Engineered features including JWT-based stateless authentication, Role-Based Access Control, live multi-currency conversions using third-party APIs, and high-performance server-side data filtering. Ensured database integrity through atomic transactional rollbacks and soft-deletion mechanisms.
