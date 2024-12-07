package ufpr.web.types.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
    public class OpenRequestDTO {
    private Long id;
    private LocalDateTime registryDate;
    private String customerName;
    private String equipmentDescription;
}
