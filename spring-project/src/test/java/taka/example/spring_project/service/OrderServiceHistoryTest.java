package taka.example.spring_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import taka.example.spring_project.dto.OrderHistoryResponse;
import taka.example.spring_project.entity.OrderDetails;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.OrderDetailsRepository;
import taka.example.spring_project.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceHistoryTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @SuppressWarnings("unchecked")
    void getOrderHistoryFetchesDetailsInOneQuery() {
        UUID userId = UUID.randomUUID();
        OrderHistory firstOrder = orderHistory("ORD-1", userId);
        OrderHistory secondOrder = orderHistory("ORD-2", userId);
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(orderRepository.findByUserId(any(), any()))
                .thenReturn(new PageImpl<>(List.of(firstOrder, secondOrder), pageRequest, 2));
        when(orderDetailsRepository.findByOrderNumberIn(List.of("ORD-1", "ORD-2")))
                .thenReturn(List.of(
                        orderDetails("ORD-1", 1),
                        orderDetails("ORD-2", 2)));

        OrderHistoryResponse response = orderService.getOrderHistory(userId, 0, 10);

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
