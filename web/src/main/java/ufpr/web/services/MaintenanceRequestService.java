package ufpr.web.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.repositories.MaintenanceRequestRepository;
import ufpr.web.types.enums.RequestStatus;

@Service
public class MaintenanceRequestService {

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    public List<MaintenanceRequest> findRequestsByUserId(Long customerId) {
        return maintenanceRequestRepository.findByCustomerIdOrderByRegistryDateAsc(customerId);
    }

    public MaintenanceRequest findById(Long requestId) {
        return maintenanceRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public MaintenanceRequest save(MaintenanceRequest request) {
        return maintenanceRequestRepository.save(request);
    }

    public List<MaintenanceRequest> findRequestsByStatus(RequestStatus status) {
        return maintenanceRequestRepository.findByStatusOrderByRegistryDateAsc(status);
    }

    public List<MaintenanceRequest> findFilteredRequests(String filterType, LocalDate startDate, LocalDate endDate, Long employeeId) {
        
        if ("HOJE".equalsIgnoreCase(filterType)) {
            LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            return maintenanceRequestRepository.findFilteredRequests(
                null,
                todayStart, 
                todayEnd,
                employeeId
            );
        }
        
        if ("PERÍODO".equalsIgnoreCase(filterType)) {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Start and end dates are required for PERÍODO filter");
            }
            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
            return maintenanceRequestRepository.findFilteredRequests(
                null,
                startDateTime, 
                endDateTime,
                employeeId
            );
        }
        
        if ("TODAS".equalsIgnoreCase(filterType)) {
            return maintenanceRequestRepository.findFilteredRequests(
                null,
                null,
                null,
                employeeId
            );
        }
        
        throw new IllegalArgumentException("Invalid filter type. Use HOJE, PERÍODO, or TODAS.");
    }

    public List<Map<String, Object>> calculateRevenueByEquipmentCategory() {
        return maintenanceRequestRepository.calculateRevenueByEquipmentCategory(RequestStatus.CONCLUIDA);
    }

    public List<Map<String, Object>> calculateRevenueByDay(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null 
            ? LocalDateTime.of(startDate, LocalTime.MIN) 
            : null;
        LocalDateTime endDateTime = endDate != null 
            ? LocalDateTime.of(endDate, LocalTime.MAX) 
            : null;
        
        return maintenanceRequestRepository.calculateRevenueByDay(
            RequestStatus.CONCLUIDA, 
            startDateTime, 
            endDateTime
        );
    }
}
