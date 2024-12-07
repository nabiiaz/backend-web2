package ufpr.web.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ufpr.web.entities.Address;
import ufpr.web.entities.Customer;
import ufpr.web.entities.Employee;
import ufpr.web.repositories.CustomerRepository;
import ufpr.web.repositories.EmployeeRepository;
import ufpr.web.security.JwtTokenProvider;
import ufpr.web.types.dtos.AuthRequestDTO;
import ufpr.web.types.dtos.AuthResponseDTO;
import ufpr.web.types.dtos.CustomerDTO;
import ufpr.web.types.dtos.EmployeeDTO;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Transactional
    public Customer registerCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (customerRepository.existsByCpf(customerDTO.getCpf())) {
            throw new IllegalArgumentException("CPF already in use");
        }

        String rawPassword = String.format("%04d", new Random().nextInt(10000));
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        Address address = Address.builder()
            .zipCode(customerDTO.getZipCode())
            .street(customerDTO.getStreet())
            .neighborhood(customerDTO.getNeighborhood())
            .city(customerDTO.getCity())
            .state(customerDTO.getState())
            .number(customerDTO.getAddressNumber())
            .complement(customerDTO.getAddressComplement())
            .build();

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setCpf(customerDTO.getCpf());
        customer.setPhone(customerDTO.getPhone());
        customer.setPassword(encryptedPassword);
        customer.setAddress(address);
        customer.setRole("CUSTOMER");
        customer.setActive(true);

        customerRepository.save(customer);
        // sendEmail(customer.getEmail(), rawPassword);

        System.out.println(rawPassword);
        return customer;
    }

    @Transactional
    public Employee registerEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String encryptedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setPassword(encryptedPassword);
        employee.setRole("EMPLOYEE");
        employee.setActive(true);

        return employeeRepository.save(employee);
    }

    public AuthResponseDTO loginCustomer(AuthRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            )
        );

        Customer customer = customerRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtTokenProvider.generateToken(authentication);

        System.out.println("*****************JWT TOKEN************************");
        System.out.println(token);
        System.out.println("**************************************************");

        return new AuthResponseDTO(
            customer.getId(), 
            customer.getName(), 
            customer.getEmail(), 
            customer.getRole(), 
            token
        );
    }

    public AuthResponseDTO loginEmployee(AuthRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            )
        );

        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponseDTO(
            employee.getId(), 
            employee.getName(), 
            employee.getEmail(), 
            employee.getRole(), 
            token
        );
    }

    private void sendEmail(String to, String rawPassword) throws RuntimeException{

        try {
            emailService.sendSimpleMessage(to,
                "Senha para Manuntenção de Equipamentos do TADS", 
                    "Sua senha de quatro dígitos é: " + rawPassword);
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }
    }
}