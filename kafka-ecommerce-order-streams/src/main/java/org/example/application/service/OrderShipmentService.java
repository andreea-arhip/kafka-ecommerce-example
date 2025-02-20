package org.example.application.service;

import com.example.avro.OrderTotalEvent;
import com.example.avro.ShipmentSentEvent;
import com.example.avro.ShipmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
public class OrderShipmentService {

    private static final Double SHIPMENT_PRIORITY_THRESHOLD = 500.0;

    @Bean
    public Function<KStream<String, OrderTotalEvent>, KStream<String, ShipmentSentEvent>> processShipments() {
        return orderTotals -> orderTotals
                .peek((key, value) -> log.info("Creating shipment for customer: {}", value.getCustomerId()))
                .mapValues(this::createShipmentEvent);
    }

    private ShipmentSentEvent createShipmentEvent(OrderTotalEvent orderTotal) {
        return new ShipmentSentEvent(
                UUID.randomUUID().toString(),
                orderTotal.getOrderId(),
                determineShipmentStatus(orderTotal.getTotalAmount())
        );
    }

    private ShipmentStatus determineShipmentStatus(double totalAmount) {
        return totalAmount >= SHIPMENT_PRIORITY_THRESHOLD ? ShipmentStatus.HIGH_PRIORITY : ShipmentStatus.LOW_PRIORITY;
    }
}
