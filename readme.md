# 💳 Spring Boot Banking API

A secure banking system built with Spring Boot that supports user registration, authentication via JWT, account creation, deposits, withdrawals, transfers, and viewing transactions.

---

## 📦 Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- Hibernate / JPA
- MySQL
- Maven

---

## ⚙️ Setup Instructions

### ✅ Prerequisites

- Java 17+
- Maven
- MySQL (or compatible RDBMS)
- Postman (for testing)

### 🔧 Configuration

1. **MySQL Database Setup:**

```sql
CREATE DATABASE banker;
```

2. **Edit `application.properties`:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banker
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## 🚀 Build and Run

```bash
# Clone the repo
git clone https://github.com/your-username/banker.git
cd banker

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

App will start on: `http://localhost:9090`

---

## 🔐 Authentication

- All endpoints (except register and login) require a **JWT Bearer Token**.
- Add it to Postman → **Authorization** tab → type: `Bearer Token`.

---

## 📮 API Endpoints

> Base URL: `http://localhost:9090`

### 🧑 User

| Endpoint | Method | Query Params | Auth | Description |
|---------|--------|---------------|------|-------------|
| `/api/users/register` | `POST` | `name`, `password` | ❌ | Register a new user |
| `/api/users/login` | `POST` | `name`, `password` | ❌ | Login & get JWT token |

**Login Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1..."
}
```

---

### 🏦 Account

| Endpoint | Method | Query Params | Auth | Description |
|---------|--------|---------------|------|-------------|
| `/api/accounts/create` | `POST` | None | ✅ | Create account for logged-in user |
| `/api/accounts` | `GET` | `accountId` | ✅ | Get account details |
| `/api/accounts/deposit` | `POST` | `accountId`, `amount` | ✅ | Deposit money |
| `/api/accounts/withdraw` | `POST` | `accountId`, `amount` | ✅ | Withdraw money |
| `/api/accounts/transfer` | `POST` | `fromId`, `toId`, `amount` | ✅ | Transfer funds |

---

### 📑 Transactions

| Endpoint | Method | Query Params | Auth | Description |
|---------|--------|---------------|------|-------------|
| `/api/transactions` | `GET` | `accountId` | ✅ | Get all transactions for the account |

---

## 🧪 Postman Setup

1. Register a user:
   - `POST /api/users/register?name=yourname&password=yourpass`

2. Login to get token:
   - `POST /api/users/login?name=yourname&password=yourpass`

3. Set token in **Authorization** tab:
   - Type: `Bearer Token`
   - Token: *paste the received token*

4. Call secured endpoints like `/api/accounts/create`

---

## ✅ Notes

- Passwords are securely **hashed using BCrypt** before saving.
- Only the **owner of an account** can access or modify it.
- All query params are used; no path params.

---

## 👨‍💻 Author

Akshat Shah  
GitHub: [@akshatrshah](https://github.com/akshatrshah)  
LinkedIn: [linkedin.com/in/akshat-shah-3542201ba](https://linkedin.com/in/akshat-shah-3542201ba)