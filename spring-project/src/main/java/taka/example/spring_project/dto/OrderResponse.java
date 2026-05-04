package taka.example.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String orderNumber;
    private String status;
    private String message;
}
