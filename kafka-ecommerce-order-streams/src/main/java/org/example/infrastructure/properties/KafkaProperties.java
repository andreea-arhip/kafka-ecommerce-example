package org.example.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.streams")
public class KafkaProperties {

    private String applicationId;
    private String bootstrapServers;
    private Map<String, String> properties;
}
