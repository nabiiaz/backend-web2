package ufpr.web.init;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import ufpr.web.services.AuthService;
import ufpr.web.services.EquipmentCategoryService;
import ufpr.web.services.MaintenanceRequestService;
import ufpr.web.services.CustomerService;
import ufpr.web.services.EmployeeService;
import ufpr.web.types.dtos.CustomerDTO;
import ufpr.web.types.dtos.EmployeeDTO;
import ufpr.web.types.dtos.EquipmentCategoryDTO;
import ufpr.web.types.dtos.MaintenanceRequestDTO;
import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.types.enums.RequestStatus;
import ufpr.web.controllers.RequestController;
import ufpr.web.types.dtos.QuoteRequestDTO;
import ufpr.web.types.dtos.MaintainDTO;

@Component
@AllArgsConstructor
public class dataLoader implements ApplicationRunner{
    
    @Autowired
    private AuthService authService;

    @Autowired
    private EquipmentCategoryService equipmentCategoryService;

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RequestController requestController;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        authService.registerCustomer(CustomerDTO.builder()
            .name("João")
            .cpf("10120230344")
            .email("joao@email.com")
            .phone("41999422222")
            .street("Rua Coronel Dulcidio")
            .zipCode("80000-240")
            .addressComplement("casa")
            .addressNumber("333")
            .city("Curitiba")
            .neighborhood("Batel")
            .state("Parana")
            .build()
        );

        authService.registerCustomer(CustomerDTO.builder()
            .name("José")
            .cpf("11122233344455")
            .email("jose@email.com")
            .phone("41999433333")
            .street("Rua XV de Novembro")
            .zipCode("80020-310")
            .addressComplement("apto 401")
            .addressNumber("123")
            .city("Curitiba")
            .neighborhood("Centro")
            .state("Parana")
            .build()
        );

        authService.registerCustomer(CustomerDTO.builder()
            .name("Joana")
            .cpf("99988877766")
            .email("joana@email.com")
            .phone("41999444444")
            .street("Avenida Silva Jardim")
            .zipCode("80230-000")
            .addressComplement("bloco B")
            .addressNumber("1500")
            .city("Curitiba")
            .neighborhood("Água Verde")
            .state("Parana")
            .build()
        );

        authService.registerCustomer(CustomerDTO.builder()
            .name("Joaquina")
            .cpf("12345678911")
            .email("joaquina@email.com")
            .phone("41999455555")
            .street("Rua Padre Anchieta")
            .zipCode("80410-030")
            .addressComplement("casa")
            .addressNumber("2000")
            .city("Curitiba")
            .neighborhood("Bigorrilho")
            .state("Parana")
            .build()
        );

        authService.registerEmployee(EmployeeDTO.builder()
            .name("Maria")
            .birthDate(LocalDate.now())
            .email("maria@email.com")
            .password("maria")
            .build()
        );

        authService.registerEmployee(EmployeeDTO.builder()
            .name("Mário")
            .birthDate(LocalDate.now())
            .email("mario@email.com")
            .password("mario")
            .build()
        );

        equipmentCategoryService.createCategory(EquipmentCategoryDTO.builder()
            .name("Notebook")
            .build()
        );
        equipmentCategoryService.createCategory(EquipmentCategoryDTO.builder()
            .name("Desktop")
            .build()
        );
        equipmentCategoryService.createCategory(EquipmentCategoryDTO.builder()
            .name("Impressora")
            .build()
        );
        equipmentCategoryService.createCategory(EquipmentCategoryDTO.builder()
            .name("Mouse")
            .build()
        );
        equipmentCategoryService.createCategory(EquipmentCategoryDTO.builder()
            .name("Teclado")
            .build()
        );
        
        MaintenanceRequest request1 = maintenanceRequestService.save(
            MaintenanceRequest.builder()
                .equipmentDescription("Notebook Dell Latitude")
                .equipmentCategory(equipmentCategoryService.findByName("Notebook"))
                .equipmentDefect("Não liga")
                .status(RequestStatus.ABERTA)
                .registryDate(LocalDateTime.now().minusDays(5))
                .customer(customerService.findByCpf("10120230344"))
                .build()
        );


        MaintenanceRequest request2 = maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Notebook Acer")
            .equipmentCategory(equipmentCategoryService.findByName("Notebook"))
            .equipmentDefect("Tela quebrada")
            .status(RequestStatus.ABERTA)
            .registryDate(LocalDateTime.now().minusDays(4))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        MaintenanceRequest request3 = maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Desktop Lenovo")
            .equipmentCategory(equipmentCategoryService.findByName("Desktop"))
            .equipmentDefect("Fonte queimada")
            .status(RequestStatus.ABERTA)
            .registryDate(LocalDateTime.now().minusDays(3))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        MaintenanceRequest request4 = maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Mouse Razer")
            .equipmentCategory(equipmentCategoryService.findByName("Mouse"))
            .equipmentDefect("Scroll com defeito")
            .status(RequestStatus.ABERTA)
            .registryDate(LocalDateTime.now().minusDays(2))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        MaintenanceRequest request5 = maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Teclado Corsair")
            .equipmentCategory(equipmentCategoryService.findByName("Teclado"))
            .equipmentDefect("LEDs queimados")
            .status(RequestStatus.ABERTA)
            .registryDate(LocalDateTime.now().minusDays(1))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        MaintenanceRequest request6 = maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Impressora HP")
            .equipmentCategory(equipmentCategoryService.findByName("Impressora"))
            .equipmentDefect("Papel atolando")
            .status(RequestStatus.ABERTA)
            .registryDate(LocalDateTime.now())
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        MaintenanceRequest request7 = maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Mouse Logitech")
            .equipmentCategory(equipmentCategoryService.findByName("Mouse"))
            .equipmentDefect("Botão do meio queimado")
            .status(RequestStatus.ABERTA) 
            .registryDate(LocalDateTime.now())
            .customer(customerService.findByCpf("11122233344455"))
            .build()
        );

        // Now update their status using controller methods
        // For request2 (ORÇADA)
        requestController.createQuote(request2.getId(), new QuoteRequestDTO(employeeService.findFirst().getId(), 150.0));

        // For request3 (APROVADA)
        requestController.createQuote(request3.getId(), new QuoteRequestDTO(employeeService.findFirst().getId(), 200.0));
        requestController.approveService(request3.getId());

        // For request4 (REJEITADA)
        requestController.createQuote(request4.getId(), new QuoteRequestDTO(employeeService.findFirst().getId(), 300.0));
        requestController.rejectService(request4.getId());

        // For request5 (ARRUMADA)
        requestController.createQuote(request5.getId(), new QuoteRequestDTO(employeeService.findFirst().getId(), 180.0));
        requestController.approveService(request5.getId());
        requestController.doMaintenance(request5.getId(), new MaintainDTO(employeeService.findFirst().getId(), "Manutenção realizada", "Cuidado ao usar"));

        // For request6 (PAGA)
        requestController.createQuote(request6.getId(), new QuoteRequestDTO(employeeService.findFirst().getId(), 250.0));
        requestController.approveService(request6.getId());
        requestController.doMaintenance(request6.getId(), new MaintainDTO(employeeService.findFirst().getId(), "Manutenção realizada", "Limpar periodicamente"));
        requestController.payService(request6.getId());
    }

}
