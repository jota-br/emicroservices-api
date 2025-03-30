# Product Microservice

### Dependencies
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Data MongoDB
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
    class Product {
        +String id
        +String uuid
        +String name
        +String description
        +BigDecimal price
        +boolean isActive
        +int version
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
        +List<Category> categories
        +List<Image> images
    }

    class Image {
        +String imagePath
        +boolean isMain
    }

    class Category {
        +String name
        +String description
        +boolean isActive
    }

    Product "1" --> "many" Image : contains
    Product "1" --> "many" Category : belongs to
```