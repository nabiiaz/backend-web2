package ufpr.web.types.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRequestDTO {
    
    private Double quoteAmount;
    private Long employeeId;
    
}
