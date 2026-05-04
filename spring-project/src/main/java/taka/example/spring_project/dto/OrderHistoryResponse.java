package taka.example.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderHistoryResponse {
    private List<OrderDto> content;
    private PageableDto pageableDto;
}
