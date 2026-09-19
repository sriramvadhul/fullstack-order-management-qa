# OrderFlow Test Cases

## Authentication

| ID       | Scenario                                 | Expected Result       |
| -------- | ---------------------------------------- | --------------------- |
| AUTH-001 | Login with valid credentials             | User is authenticated |
| AUTH-002 | Login with invalid password              | Authentication fails  |
| AUTH-003 | Login with unknown user                  | Authentication fails  |
| AUTH-004 | Request protected endpoint without token | 401 Unauthorized      |
| AUTH-005 | Request admin endpoint as customer       | 403 Forbidden         |

---

## Product Management

| ID       | Scenario                         | Expected Result  |
| -------- | -------------------------------- | ---------------- |
| PROD-001 | Retrieve existing product        | Product returned |
| PROD-002 | Retrieve invalid product ID      | 404 response     |
| PROD-003 | Create valid product             | Product created  |
| PROD-004 | Create product with invalid data | Validation error |
| PROD-005 | Update existing product          | Product updated  |
| PROD-006 | Delete existing product          | Product removed  |

---

## Cart

| ID       | Scenario                           | Expected Result   |
| -------- | ---------------------------------- | ----------------- |
| CART-001 | Add valid product to cart          | Product added     |
| CART-002 | Add product with valid quantity    | Quantity accepted |
| CART-003 | Add product with zero quantity     | Validation error  |
| CART-004 | Add product with negative quantity | Validation error  |
| CART-005 | Add nonexistent product            | Error returned    |
| CART-006 | Remove cart item                   | Item removed      |

---

## Orders

| ID        | Scenario                                   | Expected Result  |
| --------- | ------------------------------------------ | ---------------- |
| ORDER-001 | Create order with valid cart               | Order created    |
| ORDER-002 | Create order with empty cart               | Request rejected |
| ORDER-003 | Retrieve existing order                    | Order returned   |
| ORDER-004 | Retrieve invalid order                     | Error returned   |
| ORDER-005 | Customer accesses another customer's order | Access denied    |
| ORDER-006 | Admin retrieves orders                     | Orders returned  |

---

## Regression Testing

Regression testing should be executed after changes to:

* Authentication
* Product management
* Cart functionality
* Order management
* Database entities
* Security configuration

---

## Test Data Requirements

Test data should include:

* Valid customer
* Valid administrator
* Invalid user
* Valid products
* Out-of-stock/invalid products where applicable
* Empty cart
* Cart containing products
* Existing order
* Invalid order ID
