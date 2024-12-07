package ufpr.web.types.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintainDTO {
    private Long employeeId;
    private String maintenanceDescription;
    private String customerOrientation;
}
