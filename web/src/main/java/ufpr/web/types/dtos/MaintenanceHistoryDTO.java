package ufpr.web.types.dtos;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpr.web.types.enums.RequestStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceHistoryDTO {
    private Long id;
    private Long requestId;
    private RequestStatus status;
    private String action;
    private Long employeeId;
    private Date actionDate;
}
