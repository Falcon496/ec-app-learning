package taka.example.spring_project.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                details.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed", details);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), Map.of());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                Map.of());
    }

    private ResponseEntity<ProblemDetail> buildResponse(
            HttpStatus status,
            String code,
            String message,
            Map<String, String> details) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(code);
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("details", details);
        return ResponseEntity.status(status).body(problemDetail);
    }
}
