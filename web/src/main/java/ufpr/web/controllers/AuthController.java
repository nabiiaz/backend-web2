package ufpr.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ufpr.web.entities.Customer;
import ufpr.web.entities.Employee;
import ufpr.web.services.AuthService;
import ufpr.web.types.dtos.AuthRequestDTO;
import ufpr.web.types.dtos.AuthResponseDTO;
import ufpr.web.types.dtos.CustomerDTO;
import ufpr.web.types.dtos.EmployeeDTO;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = authService.registerCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PostMapping("/register/employee")
    public ResponseEntity<Employee> registerEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = authService.registerEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}