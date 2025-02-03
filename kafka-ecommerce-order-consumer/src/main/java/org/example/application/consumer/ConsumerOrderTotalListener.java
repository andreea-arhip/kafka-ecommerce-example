package org.example.application.consumer;

import com.example.avro.OrderTotalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


@Slf4j
@Component
public class ConsumerOrderTotalListener {

    private final ConcurrentHashMap<String, Double> consumerOrderTotals = new ConcurrentHashMap<>();

    @Bean
    public Consumer<OrderTotalEvent> processOrderTotals() {
        return event -> {
            consumerOrderTotals.put(
                    String.valueOf(event.getCustomerId()),
                    event.getTotalAmount()
            );
            log.info("Updated total for customer {}: {}", event.getCustomerId(), event.getTotalAmount());
        };
    }

    public Double getOrderTotalByConsumerId(String consumerId) {
        return consumerOrderTotals.getOrDefault(consumerId, 0.0);
    }

    public ConcurrentHashMap<String, Double> getAllConsumerOrderTotals() {
        return consumerOrderTotals;
    }
}
