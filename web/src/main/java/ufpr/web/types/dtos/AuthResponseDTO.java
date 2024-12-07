package ufpr.web.types.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String token;
}