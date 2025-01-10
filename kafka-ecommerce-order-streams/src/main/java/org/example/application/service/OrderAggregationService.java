package org.example.application.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.example.application.topology.OrderTopology;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class OrderAggregationService {

    private final Properties kafkaStreamsProperties;
    private final OrderTopology orderTopology;

    private KafkaStreams kafkaStreams;

    @PostConstruct
    public void startOrderAggregationStream() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        orderTopology.buildOrderTopology(streamsBuilder);
        Topology topology = streamsBuilder.build();

        kafkaStreams = new KafkaStreams(topology, kafkaStreamsProperties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    @PreDestroy
    public void stop() {
        if (kafkaStreams != null) {
            kafkaStreams.close();
        }
    }
}
