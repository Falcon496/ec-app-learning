package taka.example.spring_project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taka.example.spring_project.dto.MemberStatusResponse;
import taka.example.spring_project.entity.MemberStatus;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.MemberStatusRepository;
import taka.example.spring_project.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MemberStatusService {

    private final MemberStatusRepository memberStatusRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public MemberStatusService(MemberStatusRepository memberStatusRepository, OrderRepository orderRepository) {
        this.memberStatusRepository = memberStatusRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public MemberStatusResponse calculateAndUpdateMemberStatus(UUID userId, String orderNumber) {
        OrderHistory order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found for order number: " + orderNumber));

        int earnedPoints = calculatePoint(order.getTotalPrice());

        MemberStatus memberStatus = memberStatusRepository.findByUserId(userId)
                .orElse(MemberStatus.builder()
                        .userId(userId)
                        .totalPoints(0)
                        .rank("Bronze")
                        .build());
        memberStatus.setTotalPoints(memberStatus.getTotalPoints() + earnedPoints);

        int totalPointsLast3Months = calculateTotalPointsLast3Months(userId);
        String newRank = determineRank(totalPointsLast3Months);
        memberStatus.setRank(newRank);

        memberStatusRepository.save(memberStatus);

        return new MemberStatusResponse(userId, memberStatus.getTotalPoints(), memberStatus.getRank());
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

    private int calculateTotalPointsLast3Months(UUID userId) {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return orderRepository.sumPointsForUserSince(userId, threeMonthsAgo);
    }

    private int calculatePoint(Integer totalPrice) {
        return totalPrice / 100;
    }

    public MemberStatusResponse getMemberStatus(UUID userId) {
        MemberStatus memberStatus = memberStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));
        return new MemberStatusResponse(userId, memberStatus.getTotalPoints(), memberStatus.getRank());
    }
}
