package org.example.application.service;

import com.example.avro.FraudAlertEvent;
import com.example.avro.OrderCreatedEvent;
import com.example.avro.OrderTotalEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerOrderTotalService {

    private final SpecificAvroSerde<OrderCreatedEvent> orderAvroSerde;
    private final SpecificAvroSerde<OrderTotalEvent> orderTotalAvroSerde;
    private static final double FRAUD_THRESHOLD = 5000.0;

    @Bean
    public Function<KStream<String, OrderCreatedEvent>, KStream<?, ?>[]> processOrders() {
        return orders -> {
            KStream<String, OrderTotalEvent> orderTotalsPerCustomer = aggregateOrderTotals(orders).toStream();

            return new KStream[]{
                    processValidTotals(orderTotalsPerCustomer),
                    processFraudTotals(orderTotalsPerCustomer)
            };
        };
    }

    private KTable<String, OrderTotalEvent> aggregateOrderTotals(KStream<String, OrderCreatedEvent> orders) {
        return orders.peek((key, value) -> log.info("Aggregating order total for customer: {}", value.getCustomerId()))
                .map((key, order) -> KeyValue.pair(order.getCustomerId(), order))
                .groupBy((key, order) ->
                        String.valueOf(order.getCustomerId()),
                        Grouped.with(Serdes.String(), orderAvroSerde)
                ).aggregate(
                        OrderTotalEvent::new,
                        this::aggregateOrderTotal,
                        Materialized.with(Serdes.String(), orderTotalAvroSerde)
                );
    }

    private OrderTotalEvent aggregateOrderTotal(String customerId, OrderCreatedEvent order, OrderTotalEvent aggregate) {
        return new OrderTotalEvent(
                order.getCustomerId(),
                order.getOrderId(),
                aggregate.getTotalAmount() + order.getOrderAmount()
        );
    }

    private static KStream<String, OrderTotalEvent> processValidTotals(KStream<String, OrderTotalEvent> orderTotalsStream) {
        return orderTotalsStream
                .filter((customerId, total) -> total.getTotalAmount() < FRAUD_THRESHOLD)
                .peek((customerId, totalEvent) ->
                        log.info("Sending new OrderTotalEvent - customer {} with total {}", totalEvent.getCustomerId(), totalEvent.getTotalAmount())
                );
    }

    private static KStream<String, FraudAlertEvent> processFraudTotals(KStream<String, OrderTotalEvent> orderTotalsStream) {
        return orderTotalsStream
                .filter((customerId, total) -> total.getTotalAmount() >= FRAUD_THRESHOLD)
                .mapValues(CustomerOrderTotalService::createFraudAlertEvent)
                .peek((customerId, totalEvent) ->
                        log.info("Sending new FraudAlertEvent - customer {} with total {}", totalEvent.getCustomerId(), totalEvent.getTotalAmount())
                );
    }

    private static FraudAlertEvent createFraudAlertEvent(OrderTotalEvent orderTotalEvent) {
        return new FraudAlertEvent(orderTotalEvent.getCustomerId(), orderTotalEvent.getTotalAmount(), "Threshold reached!");
    }

}
