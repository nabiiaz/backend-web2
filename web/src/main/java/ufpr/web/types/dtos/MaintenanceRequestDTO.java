package ufpr.web.types.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpr.web.types.enums.RequestStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {
    
    private Long id;
    private String equipmentDescription;
    private Long equipmentCategory;
    private String equipmentDefect;
    private Double quote;
    private RequestStatus status;
    private LocalDateTime registryDate;
    private String maintenanceDescription;
    private String customerOrientation;
    private Long customerId;
    private Long employeeId;
}
