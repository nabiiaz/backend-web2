package ufpr.web.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import ufpr.web.entities.Address;
import ufpr.web.entities.Customer;
import ufpr.web.repositories.CustomerRepository;
import ufpr.web.types.dtos.CustomerDTO;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Transactional
    public void updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        customer.setEmail(customerDTO.getEmail());
        customer.setName(customerDTO.getName());
        customer.setCpf(customerDTO.getCpf());
        customer.setPhone(customerDTO.getPhone());

        Address address = customer.getAddress();
        address.setZipCode(customerDTO.getZipCode());
        address.setStreet(customerDTO.getStreet());
        address.setNeighborhood(customerDTO.getNeighborhood());
        address.setCity(customerDTO.getCity());
        address.setState(customerDTO.getState());
        address.setNumber(customerDTO.getAddressNumber());
        address.setComplement(customerDTO.getAddressComplement());

        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customer.setActive(false);
        customerRepository.save(customer);
    }

    public CustomerDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return mapToDTO(customer);
    }

    public Customer customer(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();

        for (Customer c : customers) {
            customerDTOs.add(mapToDTO(c));
        }

        return customerDTOs;
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return new CustomerDTO(
            customer.getId(),
            customer.getCpf(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress().getZipCode(),
            customer.getAddress().getStreet(),
            customer.getAddress().getNeighborhood(),
            customer.getAddress().getCity(),
            customer.getAddress().getState(),
            customer.getAddress().getNumber(),
            customer.getAddress().getComplement()
        );
    }

    public Customer findByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}