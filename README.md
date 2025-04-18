# demo e-Commerce Microservices API

RESTful API for adding and retrieving products, placing orders and authenticated route access management.

### Structure

- API Gateway using ``Spring Cloud Gateway``;
- Microservices for managing products, orders, inventory, user registration and auth;
- ``MongoDB``: ``Product``;
- ``PostgreSQL``: ``Order``, ``User`` and ``Inventory``;
- ``Spring Security`` with ``JWT``;
- Cache with ``Redis``;
- Using shared libraries:
    - [`ov-auth`](https://github.com/jotabrc/ov-auth) for Token Creation and Authentication.
    - [`ov-annotation-validator`](https://github.com/jotabrc/ov-annotation-validator) for user input validation.
    - [`ov-auth-validator`](https://github.com/jotabrc/ov-auth-validator) for user authorization validation.
    - [`ov-kafka-cp`](https://github.com/jotabrc/ov-kafka-cp) for Kafka configuration and standardized Producer.

### Status
- [x] Mockito and JUnit tests.


- API Gateway
    - [ ] Security
    - [ ] Cache
- Product
    - [x] Controller
    - [x] ExceptionHandler
    - [x] Service
    - [x] Repository
    - [x] Validation
- Order
    - [x] Controller
    - [x] ExceptionHandler
    - [x] Service
    - [x] Repository
    - [x] Validation
- Inventory
    - [x] Controller
    - [x] ExceptionHandler
    - [x] Service
    - [x] Repository
    - [x] Validation
- User
    - [x] Controller
    - [x] ExceptionHandler
    - [x] Service
    - [x] Repository
    - [X] JWT
    - [X] Login
    - [x] Validation
- Kafka
    - [X] Consumers
    - [X] Producers
    - [ ] Tests