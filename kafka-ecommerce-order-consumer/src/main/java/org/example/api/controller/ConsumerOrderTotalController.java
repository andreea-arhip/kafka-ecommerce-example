package org.example.api.controller;

import org.example.application.service.ConsumerOrderTotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/consumer-order-totals")
public class ConsumerOrderTotalController {

    private final ConsumerOrderTotalService consumerOrderTotalService;

    @Autowired
    public ConsumerOrderTotalController(ConsumerOrderTotalService consumerOrderTotalService) {
        this.consumerOrderTotalService = consumerOrderTotalService;
    }

    @GetMapping("/{consumerId}")
    public Double getOrderTotalByConsumerId(@PathVariable String consumerId) {
        return consumerOrderTotalService.getOrderTotalByConsumerId(consumerId);
    }

    @GetMapping
    public Map<String, Double> getAllConsumerOrderTotals() {
        return consumerOrderTotalService.getAllConsumerOrderTotals();
    }
}
