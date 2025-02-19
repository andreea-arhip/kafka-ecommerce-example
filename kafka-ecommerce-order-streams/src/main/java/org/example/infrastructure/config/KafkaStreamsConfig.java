package org.example.infrastructure.config;

import com.example.avro.FraudAlertEvent;
import com.example.avro.OrderCreatedEvent;
import com.example.avro.OrderTotalEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.example.infrastructure.properties.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfig {

    private final KafkaProperties kafkaProperties;
    private static final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";

    @Bean
    public SpecificAvroSerde<OrderCreatedEvent> orderAvroSerde() {
        return createSerde();
    }

    @Bean
    public SpecificAvroSerde<OrderTotalEvent> customerTotalSerde() {
        return createSerde();
    }

    @Bean
    public SpecificAvroSerde<FraudAlertEvent> fraudAlertSerde() {
        return createSerde();
    }

    private <T extends SpecificRecord> SpecificAvroSerde<T> createSerde() {
        SpecificAvroSerde<T> serde = new SpecificAvroSerde<>();
        Map<String, String> config = Map.of(
                SCHEMA_REGISTRY_URL_CONFIG,
                kafkaProperties.getConfiguration().get(SCHEMA_REGISTRY_URL_CONFIG)
        );
        serde.configure(config, false); // 'false' for value serde
        return serde;
    }
}
