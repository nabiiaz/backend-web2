package ufpr.web.types.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRequestDTO {
    private Long employeeId;
    private Double quoteAmount;
}
