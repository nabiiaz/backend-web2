package ufpr.web.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ufpr.web.entities.MaintenanceRequest;
import ufpr.web.types.enums.RequestStatus;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByCustomerIdOrderByRegistryDateAsc(Long userId);
    List<MaintenanceRequest> findByStatusOrderByRegistryDateAsc(RequestStatus status);

    @Query(value = "SELECT mr FROM MaintenanceRequest mr " +
           "WHERE (CAST(:status AS string) IS NULL OR mr.status = :status) " +
           "AND (CAST(:startDate AS timestamp) IS NULL OR mr.registryDate >= :startDate) " +
           "AND (CAST(:endDate AS timestamp) IS NULL OR mr.registryDate <= :endDate) " +
           "AND (mr.employeeId IS NULL OR mr.employeeId = :employeeId) " +
           "ORDER BY mr.registryDate ASC")
    List<MaintenanceRequest> findFilteredRequests(
        @Param("status") RequestStatus status,
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate,
        @Param("employeeId") Long employeeId
    );

    @Query("SELECT new map(ec.name as categoryName, SUM(mr.quote) as totalRevenue) " +
           "FROM MaintenanceRequest mr " +
           "JOIN mr.equipmentCategory ec " +
           "WHERE mr.status = :paidStatus " +
           "GROUP BY ec.name " +
           "ORDER BY totalRevenue DESC")
    List<Map<String, Object>> calculateRevenueByEquipmentCategory(RequestStatus paidStatus);

    @Query("SELECT new map(" +
           "FUNCTION('DATE', mr.registryDate) as day, " +
           "SUM(mr.quote) as dailyRevenue) " +
           "FROM MaintenanceRequest mr " +
           "WHERE mr.status = :paidStatus " +
           "AND (:startDate IS NULL OR mr.registryDate >= :startDate) " +
           "AND (:endDate IS NULL OR mr.registryDate <= :endDate) " +
           "GROUP BY FUNCTION('DATE', mr.registryDate) " +
           "ORDER BY day")
    List<Map<String, Object>> calculateRevenueByDay(
        @Param("paidStatus") RequestStatus paidStatus,
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
}