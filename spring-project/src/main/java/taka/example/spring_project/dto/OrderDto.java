package taka.example.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class OrderDto {
    private OffsetDateTime orderDate;
    private String orderNumber;
    private Integer totalPrice;
    private Integer totalQuantity;
    private List<OrderItem> orderItems;
}
