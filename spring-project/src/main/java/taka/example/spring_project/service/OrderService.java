package taka.example.spring_project.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import taka.example.spring_project.dto.*;
import taka.example.spring_project.entity.OrderDetails;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.OrderDetailsRepository;
import taka.example.spring_project.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        String orderNumber = generateOrderNumber();
        LocalDateTime orderDateTime = LocalDateTime.now();

        OrderHistory orderHistory = OrderHistory.builder()
                .orderNumber(orderNumber)
                .orderDate(orderDateTime)
                .userId(orderRequest.getUserId())
                .userName(orderRequest.getUserName())
                .totalPrice(orderRequest.getTotalPrice())
                .totalQuantity(orderRequest.getTotalQuantity())
                .earnedPoints(calculatePoints(orderRequest.getTotalPrice()))
                .build();
        orderRepository.save(orderHistory);

        for(OrderItem orderItem : orderRequest.getOrderItems()) {
            OrderDetails orderDetails = OrderDetails.builder()
                    .orderNumber(orderNumber)
                    .productId(orderItem.getProductId())
                    .productName(orderItem.getProductName())
                    .price(orderItem.getPrice())
                    .build();
            orderDetailsRepository.save(orderDetails);
        }

        return new OrderResponse(orderNumber, "SUCCESS", "Order has been successfully created.");
    }

    private Integer calculatePoints(Integer totalPrice) {
//        Calculate 1%
        return totalPrice / 100;
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString();
    }

    public OrderHistoryResponse getOrderHistory(UUID userId,int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "orderDate"));
        Page<OrderHistory> orderHistoryPage = orderRepository.findByUserId(userId, pageRequest);

        List<OrderDto> orderDtos = orderHistoryPage.getContent().stream()
                .map(this::convertToOrderDto)
                .toList();

        PageableDto pageableDto = new PageableDto(
                orderHistoryPage.getNumber(),
                orderHistoryPage.getSize(),
                orderHistoryPage.getTotalElements(),
                orderHistoryPage.getTotalPages()
        );

        return new OrderHistoryResponse(orderDtos, pageableDto);
    }

    private OrderDto convertToOrderDto(OrderHistory orderHistory) {
        List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderNumber(orderHistory.getOrderNumber());
        List<OrderItem> orderItems = orderDetails.stream()
                .map(this::convertToOrderItem)
                .toList();

        return new OrderDto(
                orderHistory.getOrderDate().toString(),
                orderHistory.getOrderNumber(),
                orderHistory.getTotalPrice(),
                orderHistory.getTotalQuantity(),
                orderItems);
    }

    private OrderItem convertToOrderItem(OrderDetails orderDetails) {
        return new OrderItem(
                orderDetails.getProductId(),
                orderDetails.getProductName(),
                orderDetails.getPrice()
        );
    }
}
