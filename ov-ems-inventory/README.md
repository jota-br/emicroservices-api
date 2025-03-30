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
    class Inventory {
        +long id
        +String uuid
        +String name
        +List<Location> locations
        +int version
    }

    class Location {
        +long id
        +String uuid
        +String name
        +Inventory inventory
        +List<Item> items
        +int version
    }

    class Item {
        +long id
        +String uuid
        +String productUuid
        +String productName
        +int stock
        +int reserved
        +Location location
        +LocalDateTime updatedAt
        +int version
    }

    Inventory "1" --> "many" Location : contains
    Location "1" --> "many" Item : holds
```