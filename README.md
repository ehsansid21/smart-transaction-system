# 💰 TransactX Backend - Smart Transaction Management API

A **secure, enterprise-grade financial API** built using **Spring Boot 3** and **Java 21**. This backend powers the [TransactX Frontend](https://github.com/ehsansid21/transman-frontend) and provides a robust foundation for digital wallet services.

---

## 🚀 Key Features

*   🔐 **Secure User Authentication & RBAC (JWT)**
    *   Stateless authentication using JSON Web Tokens.
    *   Role-Based Access Control distinguishing `USER` and `ADMIN` privileges.
*   💸 **Multi-Currency Money Transfer System**
    *   Real-time exchange rate integration.
    *   Atomic database transactions using `@Transactional` to ensure financial integrity.
*   📊 **High-Performance Transaction Management**
    *   Advanced JPQL `@Query` implementations for full-text search and Date-Range filtering.
*   🛡️ **Admin Portal & User Management**
    *   **Safe Deactivation (Soft Delete):** Users are deactivated via an `isActive` flag, preserving financial history.
*   🐋 **Fully Dockerized**
    *   Multi-stage Docker builds for optimized runtime images.
    *   Containerized database management.

---

## 🛠 Tech Stack

*   **Java 21 / Spring Boot 3.x**
*   **Spring Security + JWT**
*   **MySQL 8.0**
*   **JPA / Hibernate**
*   **Maven**
*   **Docker**
*   **Swagger (OpenAPI)**

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

### 1. The Easy Way (Docker Compose)
The best way to run this backend is along with its database and frontend using the `docker-compose.yml` located in the [TransactX Frontend](https://github.com/ehsansid21/transman-frontend) repository.

```bash
docker-compose up --build
```

### 2. Manual Setup
If you want to run the backend separately for development:

**Configure MySQL:**
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/transmansys
spring.datasource.username=root
spring.datasource.password=yourpassword
```

**Run the Backend:**
```bash
mvn spring-boot:run
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
