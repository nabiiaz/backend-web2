package ufpr.web.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufpr.web.entities.MaintenanceRequestHistory;

@Repository
public interface MaintenanceHistoryRepository extends JpaRepository<MaintenanceRequestHistory, Long> {
    List<MaintenanceRequestHistory> findByMaintenanceRequestId(Long requestId);
}