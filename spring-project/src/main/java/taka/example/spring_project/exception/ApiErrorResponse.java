package taka.example.spring_project.exception;

import java.util.Map;

public record ApiErrorResponse(
        String code,
        String message,
        Map<String, String> details
) {
}
