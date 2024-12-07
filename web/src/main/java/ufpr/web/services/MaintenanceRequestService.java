package ufpr.web.services;

import java.util.List;

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
}
