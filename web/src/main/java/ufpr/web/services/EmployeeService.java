package ufpr.web.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import ufpr.web.entities.Employee;
import ufpr.web.repositories.EmployeeRepository;
import ufpr.web.types.dtos.EmployeeDTO;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setEmail(employeeDTO.getEmail());
        employee.setName(employeeDTO.getName());
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setPassword(employeeDTO.getPassword());

        employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        long employeeCount = employeeRepository.count();
        if (employeeCount == 1 || employee.getId().equals(id)) {
            throw new IllegalStateException("Cannot delete the last employee or self");
        }
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    public EmployeeDTO getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return mapToDTO(employee);
    }

    public Employee employee(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public List<EmployeeDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();

        for (Employee e : employees) {
            employeeDTOs.add(mapToDTO(e));
        }

        return employeeDTOs;
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        return EmployeeDTO.builder()
            .id(employee.getId())
            .name(employee.getName())
            .email(employee.getEmail())
            .build();
    }

    public Employee findFirst() {
        return employeeRepository.findAll()
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No employees found"));
    }
}
