package org.example.application.service;

import com.example.avro.FraudAlertEvent;
import com.example.avro.OrderCreatedEvent;
import com.example.avro.OrderTotalEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
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
public class OrderAggregationService {

    private final SpecificAvroSerde<OrderCreatedEvent> orderAvroSerde;
    private static final double FRAUD_THRESHOLD = 5000.0;

    @Bean
    public Function<KStream<String, OrderCreatedEvent>, KStream<?, ?>[]> processOrders() {
        return orders -> {
            KStream<String, Double> orderTotalsPerCustomer = aggregateOrderTotals(orders).toStream();

            return new KStream[]{
                    processValidTotals(orderTotalsPerCustomer),
                    processFraudTotals(orderTotalsPerCustomer)
            };
        };
    }

    private KTable<String, Double> aggregateOrderTotals(KStream<String, OrderCreatedEvent> orders) {
        return orders.peek((key, value) -> log.info("Aggregating order total for customer: {}", value.getCustomerId()))
                .groupBy((key, order) ->
                                String.valueOf(order.getCustomerId()),
                        Grouped.with(Serdes.String(), orderAvroSerde)
                )
                .aggregate(() ->
                                0.0,
                        (customerId, order, totalAmount) -> totalAmount + order.getOrderAmount(),
                        Materialized.with(Serdes.String(), Serdes.Double())
                );
    }

    private static KStream<String, OrderTotalEvent> processValidTotals(KStream<String, Double> orderTotalsStream) {
        return orderTotalsStream
                .filter((customerId, total) -> total < FRAUD_THRESHOLD)
                .mapValues(OrderTotalEvent::new)
                .peek((customerId, totalEvent) ->
                        log.info("Sending new OrderTotalEvent - customer {} with total {}", totalEvent.getCustomerId(), totalEvent.getTotalAmount())
                );
    }

    private static KStream<String, FraudAlertEvent> processFraudTotals(KStream<String, Double> orderTotalsStream) {
        return orderTotalsStream
                .filter((customerId, total) -> total >= FRAUD_THRESHOLD)
                .mapValues(OrderAggregationService::createFraudAlertEvent)
                .peek((customerId, totalEvent) ->
                        log.info("Sending new FraudAlertEvent - customer {} with total {}", totalEvent.getCustomerId(), totalEvent.getTotalAmount())
                );
    }

    private static FraudAlertEvent createFraudAlertEvent(String customerId, Double total) {
        return new FraudAlertEvent(customerId, total, "Threshold reached!");
    }

}
