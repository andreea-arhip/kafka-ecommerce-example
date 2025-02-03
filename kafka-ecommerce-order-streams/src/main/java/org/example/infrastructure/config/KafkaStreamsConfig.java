package org.example.infrastructure.config;

import com.example.avro.OrderCreatedEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;


@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfig {

    @Value("${spring.cloud.stream.kafka.streams.binder.configuration.schema.registry.url}")
    private String schemaRegistryUrl;

    @Bean
    public SpecificAvroSerde<OrderCreatedEvent> orderAvroSerde() {
        SpecificAvroSerde<OrderCreatedEvent> serde = new SpecificAvroSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        serde.configure(config, false); // 'false' for value serde
        return serde;
    }
}
