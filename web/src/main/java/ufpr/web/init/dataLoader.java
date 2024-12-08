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
import ufpr.web.types.dtos.CustomerDTO;
import ufpr.web.types.dtos.EmployeeDTO;
import ufpr.web.types.dtos.EquipmentCategoryDTO;
import ufpr.web.types.dtos.MaintenanceRequestDTO;
import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.types.enums.RequestStatus;

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

    @Override
    public void run(ApplicationArguments args) throws Exception{
        authService.registerCustomer(CustomerDTO.builder()
            .name("João")
            .cpf("10120230344")
            .email("joão@email.com")
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
            .email("josé@email.com")
            .build()
        );

        authService.registerCustomer(CustomerDTO.builder()
            .name("Joana")
            .cpf("99988877766")
            .email("joana@email.com")
            .build()
        );

        authService.registerCustomer(CustomerDTO.builder()
            .name("Joaquina")
            .cpf("12345678911")
            .email("joaquina@email.com")
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

        maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Notebook Acer")
            .equipmentCategory(equipmentCategoryService.findByName("Notebook"))
            .equipmentDefect("Tela quebrada")
            .status(RequestStatus.ORÇADA)
            .registryDate(LocalDateTime.now().minusDays(4))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Desktop Lenovo")
            .equipmentCategory(equipmentCategoryService.findByName("Desktop"))
            .equipmentDefect("Fonte queimada")
            .status(RequestStatus.APROVADA)
            .registryDate(LocalDateTime.now().minusDays(3))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Mouse Razer")
            .equipmentCategory(equipmentCategoryService.findByName("Mouse"))
            .equipmentDefect("Scroll com defeito")
            .status(RequestStatus.REJEITADA)
            .registryDate(LocalDateTime.now().minusDays(2))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Teclado Corsair")
            .equipmentCategory(equipmentCategoryService.findByName("Teclado"))
            .equipmentDefect("LEDs queimados")
            .status(RequestStatus.ARRUMADA)
            .registryDate(LocalDateTime.now().minusDays(1))
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );

        maintenanceRequestService.save(MaintenanceRequest.builder()
            .equipmentDescription("Impressora HP")
            .equipmentCategory(equipmentCategoryService.findByName("Impressora"))
            .equipmentDefect("Papel atolando")
            .status(RequestStatus.PAGA)
            .registryDate(LocalDateTime.now())
            .customer(customerService.findByCpf("10120230344"))
            .build()
        );
    }

}
