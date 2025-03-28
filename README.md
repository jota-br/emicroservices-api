# demo e-Commerce Microservices API
RESTful API for adding and retriving products, placing orders and authenticate route access.

### Structure
- API Gateway using ``Spring Cloud Gateway``;
- Microservices for Product, Order placement and Authentication;
- ``Product`` will use ``MongoDB Document``;
- ``Order`` and User will use ``PostgreSQL``;
- Security will be handled using ``Spring Security`` using ``JWT``;
- Cache using ``Redis``;

### Status
- Product
  - work in progress