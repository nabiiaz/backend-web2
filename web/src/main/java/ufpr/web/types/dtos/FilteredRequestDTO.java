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
public class FilteredRequestDTO {
    private Long id;
    private LocalDateTime registryDate;
    private RequestStatus status;
    private String customerName;
    private String equipmentDescription;
}