# demo e-Commerce Microservices API
RESTful API for adding and retrieving products, placing orders and authenticate route access.

### Structure
- API Gateway using ``Spring Cloud Gateway``;
- Microservices for Product, Order placement and Authentication;
- ``Product`` with ``MongoDB Document``;
- ``Order`` and ``User`` with ``PostgreSQL``;
- Security will be handled using ``Spring Security`` with ``JWT``;
- Cache with ``Redis``;

### Status
- API Gateway
    - [ ] Security
- Product
    - [x] Controller
    - [x] ExceptionHandler
    - [x] Service
    - [x] Repository
    - [ ] Validation
- Order
    - [ ] Controller
    - [ ] ExceptionHandler
    - [ ] Service
    - [ ] Repository
    - [ ] Validation