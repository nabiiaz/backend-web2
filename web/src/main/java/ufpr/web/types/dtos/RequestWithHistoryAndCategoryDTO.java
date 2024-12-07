package ufpr.web.types.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestWithHistoryAndCategoryDTO {
    private MaintenanceRequestDTO maintenanceRequest;
    private List<MaintenanceHistoryDTO> history;
    private EquipmentCategoryDTO equipmentCategory;
    private CustomerDTO customer;
}
