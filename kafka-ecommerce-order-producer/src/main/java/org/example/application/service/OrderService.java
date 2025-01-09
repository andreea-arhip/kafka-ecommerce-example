package org.example.application.service;

import com.example.avro.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.example.api.request.OrderCreatedRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static org.example.application.mapping.OrderCreatedMapping.mapOrderCreatedEvent;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${kafka.topics.order-created-topic}")
    private String orderCreatedTopic;


    public void sendOrderCreatedEvent(OrderCreatedRequest request) {
        var orderCreatedEvent = mapOrderCreatedEvent(request);
        kafkaTemplate.send(orderCreatedTopic, orderCreatedEvent.getOrderId().toString() , orderCreatedEvent);
    }
}
