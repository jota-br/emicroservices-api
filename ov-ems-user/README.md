# User Microservice

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
erDiagram
    User {
        long id
        string uuid
        string username
        string email
        string salt
        string hash
        string firstName
        string lastName
        string phone
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
        int version
    }
    Role {
        long id
        string uuid
        string name
        string description
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
        int version
    }
    Address {
        long id
        string uuid
        string postalCode
        string street
        string number
        AddressType type
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
        int version
    }
    City {
        long id
        string uuid
        string name
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
        int version
    }
    State {
        long id
        string uuid
        string name
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
        int version
    }
    Country {
        long id
        string uuid
        string name
        boolean isActive
        LocalDateTime createdAt
        LocalDateTime updatedAt
        int version
    }

    User ||--o{ Address : "has"
    User ||--o| Role : "belongs to"
    Address ||--o| City : "is in"
    City ||--o| State : "belongs to"
    State ||--o| Country : "is part of"
```