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
`PUT`</br>
`/api/v1/order/update`</br>
Update `Order`.
Returns 204 No Content (success).

`POST` </br>
`/api/v1/order/add/`</br>
Add new `Order`.

`POST` </br>
`/api/v1/order/return/`</br>
Return `Order` products.

`POST` </br>
`/api/v1/order/cancel/{uuid}`</br>
Cancel `Order` with `{uuid}`.

`GET` </br>
`/api/v1/order/{uuid}`</br>
Get `Order` with `{uuid}`.

`GET` </br>
`/api/v1/order/user/{uuid}`</br>
Get `Orders` with user `{uuid}`.

All endpoints return an `ResponsePayload` with a message indicating the operation success or failure and a `ResponseBody` if retrieving data.

### In progress
- Validation will be done with annotations;
- Security will be handled by Spring Cloud Gateway;