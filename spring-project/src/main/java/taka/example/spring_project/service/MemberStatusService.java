package taka.example.spring_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.MemberStatusResponse;
import taka.example.spring_project.exception.NotFoundException;
import taka.example.spring_project.repository.MemberStatusRepository;
import taka.example.spring_project.repository.OrderRepository;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class MemberStatusService {

    private final MemberStatusRepository memberStatusRepository;
    private final OrderRepository orderRepository;
    private final Clock clock;

    @Autowired
    public MemberStatusService(
            MemberStatusRepository memberStatusRepository,
            OrderRepository orderRepository,
            Clock clock) {
        this.memberStatusRepository = memberStatusRepository;
        this.orderRepository = orderRepository;
        this.clock = clock;
    }

    @Transactional
    public Mono<MemberStatusResponse> calculateAndUpdateMemberStatus(UUID userId, String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("Order not found for order number: " + orderNumber)))
                .flatMap(order -> {
                    if (!order.getUserId().equals(userId)) {
                        return Mono.error(new IllegalArgumentException(
                                "Order does not belong to user: " + orderNumber));
                    }
                    return Mono.zip(
                                    orderRepository.sumEarnedPointsByUserId(userId).defaultIfEmpty(0),
                                    calculateTotalPointsLast3Months(userId).defaultIfEmpty(0))
                            .flatMap(points -> {
                                Integer totalPoints = points.getT1();
                                String newRank = determineRank(points.getT2());
                                return memberStatusRepository.upsertStatus(userId, totalPoints, newRank)
                                        .thenReturn(new MemberStatusResponse(userId, totalPoints, newRank));
                            });
                });
    }

    private String determineRank(int totalPointsLast3Months) {
        if (totalPointsLast3Months >= 50){
            return "Gold";
        } else if (totalPointsLast3Months >= 10){
            return "Silver";
        } else {
            return "Bronze";
        }
    }

    private Mono<Integer> calculateTotalPointsLast3Months(UUID userId) {
        OffsetDateTime threeMonthsAgo = OffsetDateTime.now(clock).minusMonths(3);
        return orderRepository.sumEarnedPointsForUserSince(userId, threeMonthsAgo);
    }

    public Mono<MemberStatusResponse> getMemberStatus(UUID userId) {
        return memberStatusRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found for id: " + userId)))
                .map(memberStatus -> new MemberStatusResponse(
                        userId,
                        memberStatus.getTotalPoints(),
                        memberStatus.getRank()));
    }
}
