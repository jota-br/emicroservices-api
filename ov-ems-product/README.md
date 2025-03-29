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
`PUT`</br>
`/api/v1/product/update`</br>
Update all `Product's` fields except price and stock.
Returns 204 No Content (success).

`PUT` </br>
`/api/v1/product/update/price/{uuid}`</br>
Updates `Product` price.
Returns 204 No Content (success).

`POST` </br>
`/api/v1/product/add`</br>
Add new `Product`.

`GET` </br>
`/api/v1/product/{name}`</br>
Get `Product` with `{name}`.

`GET` </br>
`/api/v1/product/uuid/{uuid}`</br>
Get `Product` with `{uuid}`.

`GET` </br>
`/api/v1/product/category/{category}`</br>
Get `Products` with `{category}`.

All endpoints return an `ResponsePayload` with a message indicating the operation success or failure and a `ResponseBody` if retrieving data.

### In progress
- Validation will be done with annotations;
- Security will be handled by Spring Cloud Gateway;