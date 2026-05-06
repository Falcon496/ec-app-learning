package taka.example.spring_project.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsMissingRequiredFields() {
        OrderRequest request = new OrderRequest();

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void acceptsValidRequest() {
        OrderRequest request = new OrderRequest();
        request.setUserId(UUID.randomUUID());
        request.setUserName("test-user");
        request.setTotalPrice(1000);
        request.setTotalQuantity(1);
        request.setOrderItems(List.of(new OrderItem(1, "coffee", 1000)));

        assertTrue(validator.validate(request).isEmpty());
    }
}
