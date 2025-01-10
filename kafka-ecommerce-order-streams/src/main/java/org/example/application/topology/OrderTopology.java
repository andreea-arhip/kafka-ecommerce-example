package org.example.application.topology;

import com.example.avro.OrderCreatedEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderTopology {

    private final SpecificAvroSerde<OrderCreatedEvent> orderAvroSerde;

    @Value("${spring.kafka.topics.order-created-topic}")
    private String orderCreatedTopic;

    @Value("${spring.kafka.topics.consumer-order-totals-topic}")
    private String consumerOrderTotalsTopic;

    public Topology buildOrderTopology(StreamsBuilder builder) {
        KStream<String, OrderCreatedEvent> orderStream = builder.stream(
                orderCreatedTopic,
                Consumed.with(Serdes.String(), orderAvroSerde)
        );

        KTable<String, Double> consumerOrderTotalsStream = orderStream
                .mapValues(OrderCreatedEvent::getOrderAmount)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
                .aggregate(
                        () -> 0.0,
                        (customerId, orderAmount, total) -> total + orderAmount,
                        Materialized.with(Serdes.String(), Serdes.Double())
                );

        consumerOrderTotalsStream.toStream().to(
                consumerOrderTotalsTopic,
                Produced.with(Serdes.String(), Serdes.Double())
        );

        return builder.build();
    }
}
