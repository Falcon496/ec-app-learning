package taka.example.spring_project.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsNotFoundExceptionTo404() {
        var response = handler.handleNotFoundException(new NotFoundException("not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("NOT_FOUND", response.getBody().getTitle());
        assertEquals("not found", response.getBody().getDetail());
        assertEquals("NOT_FOUND", response.getBody().getProperties().get("code"));
    }

    @Test
    void mapsIllegalArgumentExceptionTo400() {
        var response = handler.handleIllegalArgumentException(new IllegalArgumentException("bad request"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("BAD_REQUEST", response.getBody().getTitle());
        assertEquals("bad request", response.getBody().getDetail());
        assertEquals("BAD_REQUEST", response.getBody().getProperties().get("code"));
    }
}
