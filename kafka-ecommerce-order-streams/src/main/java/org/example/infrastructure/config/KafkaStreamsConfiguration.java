package org.example.infrastructure.config;

import com.example.avro.OrderCreatedEvent;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.example.infrastructure.properties.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;


@Configuration
@RequiredArgsConstructor
public class KafkaStreamsConfiguration {

    private final KafkaProperties kafkaProperties;
    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";

    @Bean
    public Properties kafkaStreamsProperties() {
        Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getApplicationId());
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);

            // Add additional properties from YAML
            props.putAll(kafkaProperties.getProperties());
            return props;
    }

    @Bean
    public SpecificAvroSerde<OrderCreatedEvent> orderAvroSerde() {
        SpecificAvroSerde<OrderCreatedEvent> serde = new SpecificAvroSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put(SCHEMA_REGISTRY_URL_CONFIG, kafkaProperties.getProperties().get(SCHEMA_REGISTRY_URL));
        serde.configure(config, false); // 'false' for value serde
        return serde;
    }
}
