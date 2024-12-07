package ufpr.web.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.types.enums.RequestStatus;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByCustomerIdOrderByRegistryDateAsc(Long userId);
    List<MaintenanceRequest> findByStatusOrderByRegistryDateAsc(RequestStatus status);
}