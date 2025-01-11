package org.example.application.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConsumerOrderTotalListener {

    private final ConcurrentHashMap<String, Double> consumerOrderTotals = new ConcurrentHashMap<>();

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.consumer-order-totals-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, Double> record) {
        System.out.printf("Consumed record: [%s, %f] ", record.key(), record.value());
        consumerOrderTotals.put(record.key(), record.value());
    }

    public Double getOrderTotalByConsumerId(String consumerId) {
        return consumerOrderTotals.get(consumerId);
    }

    public ConcurrentHashMap<String, Double> getAllConsumerOrderTotals() {
        return consumerOrderTotals;
    }
}
