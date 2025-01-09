package org.example.api;

import lombok.RequiredArgsConstructor;
import org.example.api.request.OrderCreatedRequest;
import org.example.application.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrderEvent(@RequestBody OrderCreatedRequest orderCreatedRequest) {
        orderService.sendOrderCreatedEvent(orderCreatedRequest);
        return ResponseEntity.ok("Submitted a new order!");
    }
}
