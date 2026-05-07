package taka.example.spring_project.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import taka.example.spring_project.dto.OrderHistoryResponse;
import taka.example.spring_project.dto.OrderRequest;
import taka.example.spring_project.dto.OrderResponse;
import taka.example.spring_project.service.OrderService;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<ResponseEntity<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest)
                .map(orderResponse -> ResponseEntity.status(HttpStatus.CREATED).body(orderResponse));
    }

    @GetMapping
    public Mono<ResponseEntity<OrderHistoryResponse>> getOrderHistory(
            @RequestParam UUID userId,
            @Min(0) @RequestParam(defaultValue = "0") int page,
            @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size){
        return orderService.getOrderHistory(userId, page, size)
                .map(ResponseEntity::ok);
    }
}
