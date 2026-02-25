# Student Task Manager API

A secure RESTful backend application built using Spring Boot that allows users to manage tasks with JWT-based authentication and Role-Based Access Control (RBAC).

---

## 🚀 Features

- User Registration & Login (JWT Authentication)
- Role-Based Access Control (USER / ADMIN)
- Create, Update, Delete Tasks (User-specific)
- Admin access to all users & tasks
- Global Exception Handling
- Validation with Proper API Error Response
- Secure endpoints using Spring Security
- MySQL Database integration

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- MySQL
- Lombok
- Maven

---

## 🔐 Security Architecture

- Stateless Authentication
- JWT token-based authentication
- Password encryption using BCrypt
- Role-Based Method Security using `@PreAuthorize`
- Custom JWT Filter
- Custom UserDetailsService

---

## 👥 Roles

### USER
- View own profile
- Update own profile
- Create task
- View own tasks
- Update own task
- Delete own task

### ADMIN
- View all users
- Delete users
- View all tasks

---

## 📁 Project Structure

```
controller
service
service.impl
repository
entity
dto
security
exception
config
```

---

## 📦 API Endpoints

### 🔑 Auth APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login & get JWT token |

---

### 👤 User APIs

| Method | Endpoint | Role |
|--------|----------|------|
| GET | /api/users/me | USER |
| PUT | /api/users/me | USER |
| GET | /api/users | ADMIN |
| GET | /api/users/{id} | ADMIN |
| DELETE | /api/users/{id} | ADMIN |

---

### 📌 Task APIs

| Method | Endpoint | Role |
|--------|----------|------|
| POST | /api/tasks | USER |
| GET | /api/tasks/my | USER |
| GET | /api/tasks/{id} | USER |
| PUT | /api/tasks/{id} | USER |
| DELETE | /api/tasks/{id} | USER |
| GET | /api/tasks | ADMIN |

---

## 🗄 Database Setup

Create database in MySQL:

```sql
CREATE DATABASE student_task_manager;
```

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/student_task_manager
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

## ▶️ How To Run

1. Clone the repository
2. Configure database
3. Run:

```
mvn spring-boot:run
```

Server will start at:

```
http://localhost:8080
```

---

## 🧪 Testing

You can test APIs using:
- Postman
- Swagger (if integrated)

Add JWT token in header:

```
Authorization: Bearer <your_token>
```

---

## ⚠ Error Handling

All errors return structured JSON:

```json
{
  "timestamp": "2026-02-25T15:40:12",
  "status": 404,
  "error": "Not Found",
  "message": "Task not found",
  "path": "/api/tasks/10"
}
```

---

## 📌 Author

Abhishek Patel  
Backend Developer (Spring Boot)

---

## 📄 License

This project is for learning and academic purposes.