package taka.example.spring_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.MemberStatusResponse;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.MemberStatusRepository;
import taka.example.spring_project.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class MemberStatusServiceTest {

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private MemberStatusService memberStatusService;

    @Test
    void calculateAndUpdateMemberStatusRecalculatesTotalPointsIdempotently() {
        UUID userId = UUID.randomUUID();
        String orderNumber = "ORD-1";
        OrderHistory order = OrderHistory.builder()
                .orderNumber(orderNumber)
                .orderDate(LocalDateTime.now())
                .userId(userId)
                .userName("test-user")
                .totalPrice(2000)
                .totalQuantity(1)
                .earnedPoints(20)
                .build();

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.just(order));
        when(orderRepository.sumEarnedPointsByUserId(userId)).thenReturn(Mono.just(20));
        when(orderRepository.sumEarnedPointsForUserSince(any(), any())).thenReturn(Mono.just(20));
        when(memberStatusRepository.upsertStatus(userId, 20, "Silver")).thenReturn(Mono.just(1));

        MemberStatusResponse firstResponse = memberStatusService.calculateAndUpdateMemberStatus(userId, orderNumber).block();
        MemberStatusResponse secondResponse = memberStatusService.calculateAndUpdateMemberStatus(userId, orderNumber).block();

        assertEquals(20, firstResponse.getPoints());
        assertEquals(20, secondResponse.getPoints());
        assertEquals("Silver", firstResponse.getRank());
        assertEquals("Silver", secondResponse.getRank());
        verify(memberStatusRepository, times(2)).upsertStatus(userId, 20, "Silver");
    }

    @Test
    void calculateAndUpdateMemberStatusRejectsOrderOwnedByAnotherUser() {
        UUID requestUserId = UUID.randomUUID();
        UUID orderUserId = UUID.randomUUID();
        String orderNumber = "ORD-2";
        OrderHistory order = OrderHistory.builder()
                .orderNumber(orderNumber)
                .orderDate(LocalDateTime.now())
                .userId(orderUserId)
                .userName("another-user")
                .totalPrice(1000)
                .totalQuantity(1)
                .earnedPoints(10)
                .build();

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.just(order));

        assertThrows(
                IllegalArgumentException.class,
                () -> memberStatusService.calculateAndUpdateMemberStatus(requestUserId, orderNumber).block());
        verify(memberStatusRepository, never()).findByUserId(any());
        verify(memberStatusRepository, never()).upsertStatus(any(), any(), any());
    }
}
