package taka.example.spring_project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taka.example.spring_project.dto.OrderHistoryResponse;
import taka.example.spring_project.dto.OrderRequest;
import taka.example.spring_project.dto.OrderResponse;
import taka.example.spring_project.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        try{
            OrderResponse orderResponse = orderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (Exception ex) {
            log.error("Error occurred while crating order\n detail: {}", ex.getMessage(), ex);
            OrderResponse orderResponse = new OrderResponse(null, "ERROR", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(orderResponse);
        }
    }

    @GetMapping
    public ResponseEntity<OrderHistoryResponse> getOrderHistory(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try{
            OrderHistoryResponse response = orderService.getOrderHistory(userId, page, size);
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            log.error("Error occurred while fetching order history for userId: {}\n detail: {}", userId, ex.getMessage(), ex);
            OrderHistoryResponse errorResponse = new OrderHistoryResponse(null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }
}
