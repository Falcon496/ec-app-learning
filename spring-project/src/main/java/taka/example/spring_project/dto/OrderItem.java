package taka.example.spring_project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItem {
    private Integer productId;
    private String productName;
    private Integer price;
}
