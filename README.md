# LegacyBook

LegacyBook is a full-stack application for managing family groups, posts, and user profiles. It consists of a Java Spring Boot backend and an Android client, supporting secure authentication, role-based authorization, and media uploads.

---

## 📦 Project Structure

```
LegacyBook/
├── backend/                # Java Spring Boot backend
│   └── LegacyBookBackend/
│       ├── src/
│       ├── secret/
│       └── uploads/
├── LegacyKeep/             # Android client app
│   └── app/
├── secret/                 # Shared secrets
├── uploads/                # Shared uploads
└── docker-compose.yml      # Docker orchestration
```

---

## 🚀 Features

- **User authentication** (JWT-based)
- **Role-based authorization** (admin, member, etc.)
- **Family group management** (create, join, remove members)
- **Post creation** (with image/audio upload)
- **Profile management**
- **RESTful API** for mobile and web clients
- **Integration tests** for backend endpoints

---

## 🛠 Technologies

- **Java 17**, **Spring Boot**
- **Android (Java)**
- **JWT (JSON Web Token)**
- **Spring Security**
- **Docker & Docker Compose**
- **Gradle**

---

## 🔒 Authentication & Authorization

Authentication and authorization are implemented in the backend using Spring Security and JWT tokens.  
- Secure login and registration endpoints
- Password hashing (BCrypt)
- JWT token generation and validation
- Role checks for protected endpoints

See [`LegacyBook/backend/LegacyBookBackend/src/main/java/com/backend/legacybookbackend/Services/AuthService.java`](backend/LegacyBookBackend/src/main/java/com/backend/legacybookbackend/Services/AuthService.java) and [`LegacyBook/backend/LegacyBookBackend/src/main/java/com/backend/legacybookbackend/Security/SecurityConfig.java`](backend/LegacyBookBackend/src/main/java/com/backend/legacybookbackend/Security/SecurityConfig.java).

---

## 🧪 Integration

- Integration tests for authentication, family group, and post endpoints
- Docker Compose for local integration of backend and database
- Android client integration with backend API

---

## 📝 Setup

### Prerequisites

- Java 17+
- Android Studio (for client)
- Docker (for backend/database)

### Backend

```sh
cd backend/LegacyBookBackend
./gradlew build
./gradlew bootRun
```

### Android Client

Open `LegacyKeep` in Android Studio and run on emulator/device.

### Docker

```sh
docker-compose up --build
```

---

## 📑 License

This project is for educational purposes.  
See individual folders for third-party licenses.

---
