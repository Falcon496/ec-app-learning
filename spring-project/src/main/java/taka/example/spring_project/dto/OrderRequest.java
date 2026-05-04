package taka.example.spring_project.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    private UUID userId;
    private String userName;
    private Integer totalPrice;
    private Integer totalQuantity;
    private List<OrderItem> orderItems;
}
