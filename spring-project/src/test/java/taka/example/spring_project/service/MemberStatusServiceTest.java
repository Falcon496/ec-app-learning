package taka.example.spring_project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import taka.example.spring_project.dto.MemberStatusResponse;
import taka.example.spring_project.entity.MemberStatus;
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
        MemberStatus memberStatus = MemberStatus.builder()
                .userId(userId)
                .totalPoints(10)
                .rank("Bronze")
                .build();
        OrderHistory order = OrderHistory.builder()
                .orderNumber(orderNumber)
                .orderDate(LocalDateTime.now())
                .userId(userId)
                .userName("test-user")
                .totalPrice(2000)
                .totalQuantity(1)
                .earnedPoints(20)
                .build();

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));
        when(memberStatusRepository.findByUserId(userId)).thenReturn(Optional.of(memberStatus));
        when(orderRepository.sumEarnedPointsByUserId(userId)).thenReturn(20);
        when(orderRepository.sumEarnedPointsForUserSince(any(), any())).thenReturn(20);

        MemberStatusResponse firstResponse = memberStatusService.calculateAndUpdateMemberStatus(userId, orderNumber);
        MemberStatusResponse secondResponse = memberStatusService.calculateAndUpdateMemberStatus(userId, orderNumber);

        assertEquals(20, firstResponse.getPoints());
        assertEquals(20, secondResponse.getPoints());
        ArgumentCaptor<MemberStatus> captor = ArgumentCaptor.forClass(MemberStatus.class);
        verify(memberStatusRepository, org.mockito.Mockito.times(2)).save(captor.capture());
        assertEquals(20, captor.getAllValues().get(0).getTotalPoints());
        assertEquals(20, captor.getAllValues().get(1).getTotalPoints());
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

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        assertThrows(
                IllegalArgumentException.class,
                () -> memberStatusService.calculateAndUpdateMemberStatus(requestUserId, orderNumber));
        verify(memberStatusRepository, never()).findByUserId(any());
        verify(memberStatusRepository, never()).save(any());
    }
}
