package taka.example.spring_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taka.example.spring_project.dto.OrderItem;
import taka.example.spring_project.dto.OrderRequest;
import taka.example.spring_project.entity.OrderDetails;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.OrderDetailsRepository;
import taka.example.spring_project.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderCalculatesTotalsFromItems() {
        OrderRequest request = new OrderRequest();
        request.setUserId(UUID.randomUUID());
        request.setUserName("test-user");
        request.setTotalPrice(1);
        request.setTotalQuantity(99);
        request.setOrderItems(List.of(
                new OrderItem(1, "coffee", 600),
                new OrderItem(2, "tea", 400)));

        orderService.createOrder(request);

        ArgumentCaptor<OrderHistory> orderCaptor = ArgumentCaptor.forClass(OrderHistory.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(1000, orderCaptor.getValue().getTotalPrice());
        assertEquals(2, orderCaptor.getValue().getTotalQuantity());
        assertEquals(10, orderCaptor.getValue().getEarnedPoints());

        ArgumentCaptor<OrderDetails> detailsCaptor = ArgumentCaptor.forClass(OrderDetails.class);
        verify(orderDetailsRepository, org.mockito.Mockito.times(2)).save(detailsCaptor.capture());
        assertEquals(2, detailsCaptor.getAllValues().size());
    }
}
