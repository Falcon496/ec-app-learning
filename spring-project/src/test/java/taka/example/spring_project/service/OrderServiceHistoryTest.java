package taka.example.spring_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.OrderHistoryResponse;
import taka.example.spring_project.entity.OrderDetails;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.OrderDetailsRepository;
import taka.example.spring_project.repository.OrderHistoryCommandRepository;
import taka.example.spring_project.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceHistoryTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-05-08T01:23:45Z"),
            ZoneOffset.UTC);

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private OrderHistoryCommandRepository orderHistoryCommandRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                orderRepository,
                orderDetailsRepository,
                orderHistoryCommandRepository,
                FIXED_CLOCK);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getOrderHistoryFetchesDetailsInOneQuery() {
        UUID userId = UUID.randomUUID();
        OrderHistory firstOrder = orderHistory("ORD-1", userId);
        OrderHistory secondOrder = orderHistory("ORD-2", userId);

        when(orderRepository.findByUserId(any(), any()))
                .thenReturn(Flux.just(firstOrder, secondOrder));
        when(orderRepository.countByUserId(userId)).thenReturn(Mono.just(2L));
        when(orderDetailsRepository.findByOrderNumberIn(List.of("ORD-1", "ORD-2")))
                .thenReturn(Flux.just(
                        orderDetails("ORD-1", 1),
                        orderDetails("ORD-2", 2)));

        OrderHistoryResponse response = orderService.getOrderHistory(userId, 0, 10).block();

        assertEquals(2, response.getContent().size());
        assertEquals(1, response.getContent().get(0).getOrderItems().size());
        assertEquals(1, response.getContent().get(1).getOrderItems().size());
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(orderDetailsRepository).findByOrderNumberIn(captor.capture());
        assertEquals(List.of("ORD-1", "ORD-2"), captor.getValue());
    }

    private OrderHistory orderHistory(String orderNumber, UUID userId) {
        return OrderHistory.builder()
                .orderNumber(orderNumber)
                .orderDate(LocalDateTime.now())
                .userId(userId)
                .userName("test-user")
                .totalPrice(1000)
                .totalQuantity(1)
                .earnedPoints(10)
                .build();
    }

    private OrderDetails orderDetails(String orderNumber, Integer productId) {
        return OrderDetails.builder()
                .orderNumber(orderNumber)
                .productId(productId)
                .productName("product-" + productId)
                .price(1000)
                .build();
    }
}
