package taka.example.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class OrderDto {
    private String orderDate;
    private String orderNumber;
    private Integer totalPrice;
    private Integer totalQuantity;
    private List<OrderItem> orderItems;
}
