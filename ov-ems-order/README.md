# Order Microservice

### Dependencies
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Data Jpa
- OpenAI (Swagger)
- Lombok

### Exceptions
Exceptions are handled using `RestControllerAdvice`.

### Endpoints
![img.png](docs/img.png)

All endpoints return an `ResponsePayload` with a message indicating the operation success or failure and a `ResponseBody` if retrieving data.

### In progress
- Validation will be done with annotations;
- Security will be handled by Spring Cloud Gateway;

### Diagram
```mermaid
classDiagram
    class Order {
        +long id
        +String uuid
        +String userUuid
        +String userEmail
        +String shippingAddress
        +String billingAddress
        +LocalDateTime orderDate
        +BigDecimal totalAmount
        +LocalDateTime updatedAt
        +OrderStatus status
        +List<OrderDetail> orderDetails
        +List<OrderStatusHistory> orderStatusHistories
        +int version
    }

    class OrderDetail {
        +long id
        +String uuid
        +String productUuid
        +String productName
        +int quantity
        +BigDecimal unitPrice
        +Order order
        +int version
    }

    class OrderStatusHistory {
        +long id
        +String uuid
        +Order order
        +OrderStatus orderStatus
        +LocalDateTime changedAt
        +int version
    }

    Order "1" --> "many" OrderDetail : contains
    Order "1" --> "many" OrderStatusHistory : tracks
```