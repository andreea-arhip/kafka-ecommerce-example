package org.example.application.service;

import org.example.application.consumer.ConsumerOrderTotalListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsumerOrderTotalService {

    private final ConsumerOrderTotalListener consumerOrderTotalConsumer;

    @Autowired
    public ConsumerOrderTotalService(ConsumerOrderTotalListener consumerOrderTotalConsumer) {
        this.consumerOrderTotalConsumer = consumerOrderTotalConsumer;
    }

    public Double getOrderTotalByConsumerId(String consumerId) {
        return consumerOrderTotalConsumer.getOrderTotalByConsumerId(consumerId);
    }

    public Map<String, Double> getAllConsumerOrderTotals() {
        return consumerOrderTotalConsumer.getAllConsumerOrderTotals();
    }
}
