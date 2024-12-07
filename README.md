Necessário: 
- Java 21,
- Maven,
- PostgreSQL Server na porta 5432 com database chamada 'web2'

No .env estão as configurações do Gmail Delivery Subsystem

Para testar requisições, o Swagger fornece documentação interativa na URL [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui/index.html#/)

Para tirar a autenticação com JWT, comente a configuração Http no arquivo security/SecurityConfig.java e descomente a outra

- [x] RF001 POST /auth/register/customer
- [x] RF002 POST /auth/login/customer || /auth/login/employee
- [x] RF003 GET  /requests
- [x] RF004 POST /requests
- [x] RF005 GET  /requests/:requestId
- [x] RF006 PUT  /requests/:requestId/approve
- [x] RF007 PUT  /requests/:requestId/reject
- [x] RF008 GET  /requests/:requestId
- [x] RF009 PUT  /requests/:requestId/redeem
- [x] RF010 PUT  /requests/:requestId/pay
- [x] RF011 GET  /requests/open
- [x] RF012 PUT  /requests/:requestId/quote
- [x] RF013 GET  /requests/filtered
- [x] RF014 PUT  /requests/:requestId/maintain
- [x] RF015 PUT  /requests/:requestId/redirect
- [x] RF016 PUT  /request/:requestId/finish
- [x] RF017 CRUD  EquipmentCategoryController
- [x] RF018 CRUD  EmployeeController > para criar employee usa /auth/register/employee
- [x] RF019 GET  /revenue/daily
- [x] RF020 GET  /revenue/category

