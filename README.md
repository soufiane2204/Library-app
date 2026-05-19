# 📚 Bibliothèque — Library Management API

A REST API for a Library Management System built with Spring Boot.

## Tech Stack
- Java 17 + Spring Boot
- Spring Security + JWT
- MySQL
- JUnit 5 + Mockito

## Features
- Book CRUD with validation
- Cart system
- Order & Checkout
- JWT Authentication & Authorization
- Role-based access (ROLE_USER, ROLE_ADMIN)
- Pagination & Search
- Global exception handling
- Unit & Integration tests

## Getting Started

### Prerequisites
- Java 17
- Maven
- MySQL

### Setup

1. Clone the repo
   git clone https://github.com/soufiane2204/library-app.git

2. Create the database
   CREATE DATABASE librarydb;

3. Copy the example properties
   cp src/main/resources/application.properties.example src/main/resources/application.properties

4. Fill in your database credentials

5. Run
   mvn spring-boot:run

## API Endpoints

### Auth — Public
| Method | Endpoint |
|--------|----------|
| POST | /auth/register |
| POST | /auth/login |

### Books
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /books | Public |
| GET | /books/{id} | Public |
| POST | /books | Admin |
| PUT | /books | Admin |
| DELETE | /books/{id} | Admin |

### Cart
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /cart | User |
| GET | /cart/{id} | User |
| GET | /cart/{id}/total | User |
| POST | /cart/add | User |
| PUT | /cart/update | User |
| DELETE | /cart/remove | User |
| DELETE | /cart/clear/{id} | User |

### Orders
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /orders/checkout | User |
| GET | /orders | Admin |
| GET | /orders/{id} | User |
| PUT | /orders/{id}/status | Admin |

### Categories — Public
| Method | Endpoint |
|--------|----------|
| GET | /categories |
| GET | /categories/{id} |

## Developer
Soufiane