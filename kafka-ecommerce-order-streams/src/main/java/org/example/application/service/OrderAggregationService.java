package org.example.application.service;

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

    @Bean
    public Function<KStream<String, OrderCreatedEvent>, KStream<String, OrderTotalEvent>> processOrders() {
        return orders -> {
            KTable<String, Double> orderTotalsPerCustomer = orders
                    .peek((key, value) -> log.info("Incoming order for customer: {}", value.getCustomerId()))
                    .groupBy((key, order) ->
                                    String.valueOf(order.getCustomerId()),
                            Grouped.with(Serdes.String(), orderAvroSerde)
                    ).aggregate(
                            () -> 0.0,
                            (customerId, order, totalAmount) -> totalAmount + order.getOrderAmount(),
                            Materialized.with(Serdes.String(), Serdes.Double())
                    );
            return orderTotalsPerCustomer.toStream()
                    .peek((key, value) -> log.info("Outgoing customer total: {} -> {}", key, value))
                    .mapValues(OrderTotalEvent::new);
        };
    }
}
