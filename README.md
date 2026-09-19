# OrderFlow – Full-Stack Order Management & QA Automation

OrderFlow is a full-stack order management application built to demonstrate **software development, API testing, automated testing, database validation, containerization, and CI/CD practices**.

The project combines a **React/TypeScript frontend**, **Java/Spring Boot backend**, and **PostgreSQL database**, with automated quality assurance integrated into the development workflow.

> **Project status:** In active development

---

## Project Overview

OrderFlow provides a simple e-commerce/order-management workflow where users can authenticate, browse products, manage their cart, and create orders.

The project is designed not only as an application but also as a practical **QA engineering portfolio project**, covering different levels of testing:

* Unit testing
* Integration testing
* REST API testing
* Database validation
* UI automation
* Regression testing
* Negative testing
* Performance testing
* CI/CD automation

---

## Architecture

```text
                    ┌──────────────────────┐
                    │      React + TS      │
                    │       Frontend       │
                    └──────────┬───────────┘
                               │
                               │ REST API
                               ▼
                    ┌──────────────────────┐
                    │    Spring Boot API   │
                    │       Backend        │
                    └──────────┬───────────┘
                               │
                               │ JPA / JDBC
                               ▼
                    ┌──────────────────────┐
                    │      PostgreSQL      │
                    │       Database       │
                    └──────────────────────┘

             ┌─────────────────────────────────┐
             │          QA & Automation         │
             │                                 │
             │ JUnit / Mockito                 │
             │ Postman / Newman                │
             │ Selenium WebDriver              │
             │ Testcontainers                  │
             │ JMeter                          │
             │ GitHub Actions                  │
             └─────────────────────────────────┘
```

---

## Technology Stack

### Backend

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* REST APIs
* Maven
* JUnit 5
* Mockito

### Frontend

* React
* TypeScript
* HTML
* CSS
* JavaScript

### Database

* PostgreSQL
* SQL
* JPA/Hibernate

### QA & Test Automation

* JUnit
* Mockito
* Postman
* Newman
* Selenium WebDriver
* Testcontainers
* JMeter

### DevOps

* Docker
* Docker Compose
* GitHub Actions
* Git

---

## Application Features

### Authentication

* User login
* JWT-based authentication
* Role-based authorization
* Customer and Admin roles
* Protected REST endpoints

### Product Management

* Create products
* Retrieve products
* Update products
* Delete products
* Product validation

### Shopping Cart

* Add products to cart
* Update quantities
* Remove products
* Validate quantities
* Handle invalid product/cart requests

### Order Management

* Create orders
* Retrieve orders
* Order validation
* Customer order workflows
* Administrative order management

---

# QA Strategy

The project follows a layered testing approach.

```text
                    End-to-End Tests
                           │
                     Selenium UI
                           │
                    ─────────────
                           │
                      API Tests
                           │
                    Postman/Newman
                           │
                    ─────────────
                           │
                 Integration Tests
                           │
                    Spring Boot
                           │
                    ─────────────
                           │
                    Unit Tests
                           │
                    JUnit / Mockito
                           │
                    ─────────────
                           │
                      Database
                       PostgreSQL
```

---

## Test Coverage

The project includes testing for:

### Functional Testing

* Authentication
* Product management
* Cart management
* Order management
* Authorization

### Negative Testing

Examples include:

* Invalid login credentials
* Missing authentication
* Unauthorized access
* Invalid product ID
* Invalid cart quantity
* Missing required fields
* Invalid order requests

### API Testing

REST endpoints are tested for:

* HTTP status codes
* Response body
* Validation errors
* Authentication
* Authorization
* Business rules
* Error handling

### Database Testing

Database validation includes:

* Persisted entities
* Transaction results
* Product data
* Cart data
* Order data
* Backend/API data consistency

---

# Project Structure

```text
fullstack-order-management-qa/
│
├── backend/
│   ├── src/main/
│   ├── src/test/
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   └── package.json
│
├── postman/
│   ├── OrderFlow.postman_collection.json
│   └── OrderFlow.postman_environment.json
│
├── docs/
│   ├── architecture.md
│   ├── test-strategy.md
│   └── test-cases.md
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

---

# Running the Application

## Prerequisites

Install:

* Java 21
* Maven
* Node.js
* npm
* Docker Desktop
* Git

---

## 1. Clone the repository

```bash
git clone https://github.com/sriramvadhul/fullstack-order-management-qa.git

cd fullstack-order-management-qa
```

---

## 2. Configure environment variables

Copy the example environment file:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Update the values in `.env` for your local environment.

---

## 3. Start PostgreSQL

```bash
docker compose up -d postgres
```

Check the container:

```bash
docker compose ps
```

---

## 4. Start the backend

```bash
cd backend
```

Run:

```bash
mvn spring-boot:run
```

The backend will be available at:

```text
http://localhost:8080
```

---

## 5. Start the frontend

Open another terminal:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

---

# Running Tests

## Backend Unit and Integration Tests

From the backend directory:

```bash
mvn test
```

---

## API Tests

Postman collections are available under:

```text
postman/
```

Run through Postman or Newman.

Example:

```bash
newman run postman/OrderFlow.postman_collection.json
```

---

## UI Tests

UI automation is implemented using Selenium WebDriver.

Run the UI test suite using the documented test command.

---

## Performance Tests

Performance testing is performed using Apache JMeter.

Test plans are stored under:

```text
tests/performance/
```

---

# CI/CD

GitHub Actions is used to automate the quality pipeline.

The intended pipeline is:

```text
Push / Pull Request
        │
        ▼
Build Application
        │
        ▼
Run Unit Tests
        │
        ▼
Run Integration Tests
        │
        ▼
Run API Tests
        │
        ▼
Run UI Tests
        │
        ▼
Generate Test Results
        │
        ▼
Pipeline Result
```

---

# Test Reporting

Test results are documented in the project as the automation suite evolves.

Example reporting metrics:

| Test Layer  | Tests | Passed | Failed |
| ----------- | ----: | -----: | -----: |
| Unit        |   TBD |    TBD |    TBD |
| Integration |   TBD |    TBD |    TBD |
| API         |   TBD |    TBD |    TBD |
| UI          |   TBD |    TBD |    TBD |
| Performance |   TBD |    TBD |    TBD |

> Test counts are updated as the automation suites are completed.

---

# Docker

Docker Compose is used to simplify local environment setup.

Current containerized services include:

* PostgreSQL

Additional services may be added as the project evolves.

---

# Security

The project uses:

* Spring Security
* JWT authentication
* Role-based authorization
* Protected API endpoints

Sensitive credentials are intentionally excluded from version control.

Use `.env.example` as a template for local configuration.

---

# QA Engineering Practices Demonstrated

This project is intended to demonstrate practical QA engineering skills including:

* Test planning
* Functional testing
* Regression testing
* Negative testing
* API testing
* UI automation
* Integration testing
* Database validation
* Test data handling
* Authentication/authorization testing
* Automated test execution
* CI/CD testing
* Docker-based test environments
* Defect-oriented testing

---

# Known Limitations

The project is actively developed.

Some advanced automation and reporting capabilities are still being implemented.

Current development areas include:

* Expanding automated API coverage
* Expanding Selenium UI coverage
* Performance test scenarios
* CI/CD test execution
* Automated test reporting
* Additional negative and edge-case scenarios

---

# Future Improvements

Planned improvements include:

* Expanded API regression suite
* Page Object Model for UI automation
* Parallel Selenium execution
* Automated HTML test reports
* JMeter performance baselines
* Testcontainers-based integration testing
* GitHub Actions test reporting
* Code coverage reporting
* Additional security testing

---

# Author

**Sreerama Hosahalli Venkatesh**

M.Sc. Data Science | QA & Test Automation | Data & Software Engineering

GitHub:
https://github.com/sriramvadhul

LinkedIn:
https://www.linkedin.com/in/sreerama-hosahalli/

---

## Disclaimer

This project is a personal portfolio project created for learning and demonstrating software development and QA automation practices.
