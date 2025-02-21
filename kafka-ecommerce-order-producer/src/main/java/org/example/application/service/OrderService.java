package org.example.application.service;

import com.example.avro.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.request.OrderCreatedRequest;
import org.example.application.exception.MessageSendException;
import org.example.infrastructure.BindingProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.example.application.mapping.OrderCreatedMapping.mapOrderCreatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final StreamBridge streamBridge;
    private final BindingProperties bindingProperties;

    public void sendOrderCreatedEvent(OrderCreatedRequest request) {
        var orderCreatedEvent = mapOrderCreatedEvent(request);

        var bindingName =  bindingProperties.getOrderProducer();
        var wasOrderSent = sendOrderToKafka(orderCreatedEvent, bindingName);

        if (wasOrderSent) {
            log.info("✅ Successfully sent order message to binding: {}", bindingName);
        } else {
            String errorMessage = format(
                    "❌ Failed to send order message to binding: %s. Order details: %s",
                    bindingName,
                    orderCreatedEvent
            );
            throw new MessageSendException(errorMessage);
        }
    }

    private boolean sendOrderToKafka(OrderCreatedEvent order, String bindingName) {
        Message<OrderCreatedEvent> orderCreatedEventMessage = MessageBuilder
                .withPayload(order)
                .build();

        return streamBridge.send(
                bindingProperties.getOrderProducer(),
                orderCreatedEventMessage
        );
    }
}
