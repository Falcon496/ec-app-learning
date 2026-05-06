package taka.example.spring_project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    @NotNull
    private UUID userId;

    @NotBlank
    private String userName;

    @NotNull
    @PositiveOrZero
    private Integer totalPrice;

    @NotNull
    @Positive
    private Integer totalQuantity;

    @Valid
    @NotEmpty
    private List<OrderItem> orderItems;
}
