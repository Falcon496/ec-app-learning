package taka.example.spring_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MemberStatusResponse {
    private UUID userId;
    private Integer points;
    private String rank;
}
