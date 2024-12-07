package ufpr.web.types.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedirectDTO {
    private Long employeeId;
    private Long employeeToRedirectId;
}
