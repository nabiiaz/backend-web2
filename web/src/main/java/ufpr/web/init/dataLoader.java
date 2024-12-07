package ufpr.web.init;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import ufpr.web.services.AuthService;
import ufpr.web.services.EquipmentCategoryService;
import ufpr.web.types.dtos.CustomerDTO;
import ufpr.web.types.dtos.EmployeeDTO;
import ufpr.web.types.dtos.EquipmentCategoryDTO;

@Component
@AllArgsConstructor
public class dataLoader implements ApplicationRunner{
    
    @Autowired
    private AuthService authService;

    @Autowired
    private EquipmentCategoryService equipmentCategoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        authService.registerCustomer(CustomerDTO.builder()
            .name("João")
            .cpf("10120230344")
            .email("joão@email.com")
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
        
    }

}
