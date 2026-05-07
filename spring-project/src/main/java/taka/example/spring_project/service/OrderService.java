package taka.example.spring_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.*;
import taka.example.spring_project.entity.OrderDetails;
import taka.example.spring_project.entity.OrderHistory;
import taka.example.spring_project.repository.OrderDetailsRepository;
import taka.example.spring_project.repository.OrderHistoryCommandRepository;
import taka.example.spring_project.repository.OrderRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderHistoryCommandRepository orderHistoryCommandRepository;
    private final Clock clock;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            OrderDetailsRepository orderDetailsRepository,
            OrderHistoryCommandRepository orderHistoryCommandRepository,
            Clock clock) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderHistoryCommandRepository = orderHistoryCommandRepository;
        this.clock = clock;
    }

    @Transactional
    public Mono<OrderResponse> createOrder(OrderRequest orderRequest) {
        String orderNumber = generateOrderNumber();
        LocalDateTime orderDateTime = LocalDateTime.now(clock);
        Integer totalPrice = calculateTotalPrice(orderRequest.getOrderItems());
        Integer totalQuantity = calculateTotalQuantity(orderRequest.getOrderItems());

        OrderHistory orderHistory = OrderHistory.builder()
                .orderNumber(orderNumber)
                .orderDate(orderDateTime)
                .userId(orderRequest.getUserId())
                .userName(orderRequest.getUserName())
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .earnedPoints(calculatePoints(totalPrice))
                .build();

        List<OrderDetails> orderDetails = orderRequest.getOrderItems().stream()
                .map(orderItem -> OrderDetails.builder()
                        .orderNumber(orderNumber)
                        .productId(orderItem.getProductId())
                        .productName(orderItem.getProductName())
                        .price(orderItem.getPrice())
                        .build())
                .toList();

        return orderHistoryCommandRepository.insert(orderHistory)
                .thenMany(orderDetailsRepository.saveAll(orderDetails))
                .then(Mono.just(new OrderResponse(
                        orderNumber,
                        "SUCCESS",
                        "Order has been successfully created.")));
    }

    private Integer calculatePoints(Integer totalPrice) {
        return totalPrice / 100;
    }

    private Integer calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(OrderItem::getPrice)
                .sum();
    }

    private Integer calculateTotalQuantity(List<OrderItem> orderItems) {
        return orderItems.size();
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString();
    }

    public Mono<OrderHistoryResponse> getOrderHistory(UUID userId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "orderDate"));
        Mono<List<OrderHistory>> orderHistoriesMono = orderRepository.findByUserId(userId, pageRequest).collectList();
        Mono<Long> totalElementsMono = orderRepository.countByUserId(userId);

        return orderHistoriesMono.zipWith(totalElementsMono)
                .flatMap(result -> {
                    List<OrderHistory> orderHistories = result.getT1();
                    long totalElements = result.getT2();
                    if (orderHistories.isEmpty()) {
                        return Mono.just(buildOrderHistoryResponse(
                                orderHistories,
                                List.of(),
                                pageRequest,
                                totalElements));
                    }
                    List<String> orderNumbers = orderHistories.stream()
                            .map(OrderHistory::getOrderNumber)
                            .toList();
                    return orderDetailsRepository.findByOrderNumberIn(orderNumbers)
                            .collectList()
                            .map(orderDetails -> buildOrderHistoryResponse(
                                    orderHistories,
                                    orderDetails,
                                    pageRequest,
                                    totalElements));
                });
    }

    private OrderHistoryResponse buildOrderHistoryResponse(
            List<OrderHistory> orderHistories,
            List<OrderDetails> orderDetails,
            PageRequest pageRequest,
            long totalElements) {
        Map<String, List<OrderDetails>> orderDetailsByOrderNumber = orderDetails.stream()
                .collect(Collectors.groupingBy(OrderDetails::getOrderNumber));

        List<OrderDto> orderDtos = orderHistories.stream()
                .map(orderHistory -> convertToOrderDto(
                        orderHistory,
                        orderDetailsByOrderNumber.getOrDefault(orderHistory.getOrderNumber(), List.of())))
                .toList();

        PageableDto pageableDto = new PageableDto(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                totalElements,
                calculateTotalPages(totalElements, pageRequest.getPageSize())
        );

        return new OrderHistoryResponse(orderDtos, pageableDto);
    }

    private int calculateTotalPages(long totalElements, int pageSize) {
        return pageSize == 0 ? 0 : (int) Math.ceil((double) totalElements / pageSize);
    }

    private OrderDto convertToOrderDto(OrderHistory orderHistory, List<OrderDetails> orderDetails) {
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
