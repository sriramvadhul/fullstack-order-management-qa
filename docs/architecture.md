# OrderFlow Architecture

## Overview

OrderFlow is a full-stack order management application consisting of a React frontend, Spring Boot REST API, and PostgreSQL database.

The application is designed to demonstrate both application development and software quality assurance practices.

---

## High-Level Architecture

```text
┌─────────────────────────────┐
│        React Frontend       │
│        TypeScript           │
└──────────────┬──────────────┘
               │
               │ HTTP / REST
               ▼
┌─────────────────────────────┐
│       Spring Boot API       │
│                             │
│ Controllers                 │
│ Services                    │
│ Repositories                │
│ Security                    │
└──────────────┬──────────────┘
               │
               │ JPA / Hibernate
               ▼
┌─────────────────────────────┐
│         PostgreSQL          │
└─────────────────────────────┘
```

---

## Backend Layers

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

### Controller

Responsible for:

* HTTP requests
* Request validation
* HTTP responses
* Authentication/authorization handling

### Service

Responsible for:

* Business logic
* Validation
* Order processing
* Cart operations

### Repository

Responsible for:

* Database access
* Entity persistence
* Query operations

---

## Security

Authentication is implemented using JWT.

```text
User
 ↓
Login
 ↓
Authentication
 ↓
JWT Token
 ↓
Protected API Request
 ↓
Spring Security
 ↓
Authorization
```

---

## QA Architecture

Testing is organized into multiple layers:

```text
Unit Tests
     ↓
Integration Tests
     ↓
API Tests
     ↓
UI Tests
     ↓
Performance Tests
```

The objective is to detect defects as early as possible while also validating complete user workflows.
