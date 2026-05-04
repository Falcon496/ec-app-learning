package taka.example.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageableDto {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
