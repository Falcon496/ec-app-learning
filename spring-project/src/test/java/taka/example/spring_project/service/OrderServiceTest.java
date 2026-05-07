package taka.example.spring_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.OrderItem;
import taka.example.spring_project.dto.OrderRequest;
import taka.example.spring_project.dto.OrderResponse;
import taka.example.spring_project.entity.OrderDetails;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.OrderDetailsRepository;
import taka.example.spring_project.repository.OrderHistoryCommandRepository;
import taka.example.spring_project.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2026-05-08T01:23:45Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);

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
    void createOrderCalculatesTotalsFromItems() {
        OrderRequest request = new OrderRequest();
        request.setUserId(UUID.randomUUID());
        request.setUserName("test-user");
        request.setTotalPrice(1);
        request.setTotalQuantity(99);
        request.setOrderItems(List.of(
                new OrderItem(1, "coffee", 600),
                new OrderItem(2, "tea", 400)));

        when(orderHistoryCommandRepository.insert(any(OrderHistory.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(orderDetailsRepository.saveAll(anyIterable()))
                .thenAnswer(invocation -> Flux.fromIterable(invocation.getArgument(0)));

        OrderResponse response = orderService.createOrder(request).block();

        ArgumentCaptor<OrderHistory> orderCaptor = ArgumentCaptor.forClass(OrderHistory.class);
        verify(orderHistoryCommandRepository).insert(orderCaptor.capture());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(LocalDateTime.ofInstant(FIXED_INSTANT, ZoneOffset.UTC), orderCaptor.getValue().getOrderDate());
        assertEquals(1000, orderCaptor.getValue().getTotalPrice());
        assertEquals(2, orderCaptor.getValue().getTotalQuantity());
        assertEquals(10, orderCaptor.getValue().getEarnedPoints());

        ArgumentCaptor<Iterable<OrderDetails>> detailsCaptor = ArgumentCaptor.forClass(Iterable.class);
        verify(orderDetailsRepository).saveAll(detailsCaptor.capture());
        List<OrderDetails> savedDetails = StreamSupport.stream(detailsCaptor.getValue().spliterator(), false)
                .toList();
        assertEquals(2, savedDetails.size());
    }
}
