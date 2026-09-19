# OrderFlow QA Test Strategy

## Objective

The objective of testing is to verify that OrderFlow meets functional, technical, security, and reliability expectations.

---

## Testing Levels

### Unit Testing

Frameworks:

* JUnit 5
* Mockito

Focus:

* Service logic
* Validation
* Business rules
* Controller behavior

---

### Integration Testing

Focus:

* Spring application context
* REST endpoints
* Database interaction
* Repository behavior
* Authentication

---

### API Testing

Tooling:

* Postman
* Newman

Focus:

* HTTP status codes
* Response payloads
* Authentication
* Authorization
* Validation
* Negative scenarios
* Business rules

---

### UI Testing

Tool:

* Selenium WebDriver

Focus:

* Login
* Product workflows
* Cart workflows
* Order workflows
* Error handling

---

### Performance Testing

Tool:

* Apache JMeter

Focus:

* Response time
* Concurrent requests
* Throughput
* API stability under load

---

## Test Types

### Functional Testing

Verifies that application functionality behaves according to requirements.

### Regression Testing

Verifies that changes do not break existing functionality.

### Negative Testing

Verifies that invalid inputs and unsupported operations are handled correctly.

### Boundary Testing

Verifies behavior around input limits and edge conditions.

### Authentication Testing

Verifies valid and invalid authentication scenarios.

### Authorization Testing

Verifies that users can access only the operations permitted for their roles.

---

## Defect Severity

| Severity | Description                              |
| -------- | ---------------------------------------- |
| Critical | Application or core workflow is unusable |
| High     | Major functionality is broken            |
| Medium   | Important functionality has a workaround |
| Low      | Minor functional or UI issue             |

---

## Test Environment

Typical local environment:

* Java 21
* Spring Boot
* PostgreSQL
* Docker
* Node.js
* React
* Maven

---

## Test Data

Test data should be:

* Repeatable
* Isolated where possible
* Non-sensitive
* Easy to reset
* Suitable for positive and negative scenarios
