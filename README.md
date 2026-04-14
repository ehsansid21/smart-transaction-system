# 💰 Transaction Management System (Spring Boot + JWT)

A **secure backend REST API** built using **Spring Boot** that allows users to register, authenticate, and perform money transactions between accounts.

This project demonstrates real-world backend concepts like **JWT authentication, transaction handling, validation, and REST API design**.

---

## 🚀 Features

* 🔐 User Authentication (JWT)

  * Register & Login APIs
  * Secure endpoints using JWT token

* 👤 User Management

  * Create, update, delete users
  * Fetch all users / single user

* 💸 Money Transfer System

  * Transfer money between users
  * Handles balance updates using BigDecimal
  * Prevents invalid transactions

* 📊 Transaction Management

  * Store transaction records
  * Retrieve all transactions

* 📄 API Documentation

  * Integrated Swagger UI

---

## 🛠 Tech Stack

* Java 17
* Spring Boot
* Spring Security + JWT
* MySQL
* JPA / Hibernate
* Maven
* Swagger (OpenAPI)

---

## 📂 Project Structure

```
src/main/java/transaction/example/transmansys

├── controller     # REST Controllers
├── service        # Business Logic
├── repository     # Database Access
├── entity         # Database Models
├── dto            # Request/Response DTOs
├── security       # JWT & Security Config
├── config         # App Configuration
└── exception      # Global Exception Handling
```

---

## 🔑 Authentication Flow

1. User registers via `/auth/register`
2. User logs in via `/auth/login`
3. JWT token is generated
4. Token must be sent in request headers:

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

---

### 👤 User APIs

| Method | Endpoint    | Description    |
| ------ | ----------- | -------------- |
| GET    | /users      | Get all users  |
| GET    | /users/{id} | Get user by ID |
| POST   | /users      | Create user    |
| PUT    | /users/{id} | Update user    |
| DELETE | /users/{id} | Delete user    |

---

### 💸 Transaction APIs

| Method | Endpoint              | Description          |
| ------ | --------------------- | -------------------- |
| POST   | /transaction/transfer | Transfer money       |
| GET    | /transaction          | Get all transactions |

---

## 💡 Example Request

### Transfer Money

```
POST /transaction/transfer?senderId=1&receiverId=2&amount=500
```

---

## 🧪 Testing

You can test APIs using:

* Postman
* Swagger UI

Swagger URL:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Setup Instructions

### 1. Clone the repository

```
git clone https://github.com/YOUR_USERNAME/transmansys.git
cd transmansys
```

---

### 2. Configure MySQL

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/transmansys
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

### 3. Run the project

```
mvn spring-boot:run
```

---

## ⚠️ Important Notes

* Balance should not be NULL in database
* Always send JWT token for secured APIs
* Amount must be greater than 0

---

## 📈 Future Improvements

* Transaction history per user
* Add timestamps in transactions
* Role-based authorization (ADMIN / USER)
* Pagination and filtering
* Unit & integration testing

---

## 🧑‍💻 Author

Ehsan Siddiqui

---

## ⭐ Resume Highlight

Built a secure Transaction Management System using Spring Boot with JWT authentication, enabling user management and real-time money transfer with proper validation and database persistence.
