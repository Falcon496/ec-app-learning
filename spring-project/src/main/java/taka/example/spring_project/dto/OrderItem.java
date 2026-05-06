package taka.example.spring_project.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @NotNull
    @Positive
    private Integer productId;

    @NotBlank
    private String productName;

    @NotNull
    @PositiveOrZero
    private Integer price;
}
