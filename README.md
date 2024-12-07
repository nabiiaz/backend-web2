Necessário: 
- Java 21,
- Maven,
- MySQL Server na porta 3306 com database chamada 'web2'

No .env estão as configurações do Gmail Delivery Subsystem

Para testar requisições, o Swagger fornece documentação interativa na URL [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui/index.html#/)

Para tirar a autenticação com JWT, comente a configuração Http no arquivo security/SecurityConfig.java e descomente a outra

- [x] RF001 /auth/register/customer
- [x] RF002 /auth/login/customer || /auth/login/employee
- [x] RF003 /requests?userId=
- [x] RF004 /requests
- [x] RF005 /requests/:requestId
- [x] RF006 /requests/:requestId/approve
- [x] RF007 /requests/:requestId/reject
- [x] RF008 /requests/:requestId > falta informação do funcionário
- [x] RF009 /requests/:requestId/redeem
- [x] RF010 /requests/:requestId/pay
- [x] RF011 /request/open
- [x] RF012 /request/quote
- [ ] RF013
- [ ] RF014
- [ ] RF015
- [x] RF016 /request/:requestId/finish
- [x] RF017 EquipmentCategoryController
- [x] RF018 EmployeeController > para criar employee usa /auth/register/employee
- [ ] RF019
- [ ] RF020

