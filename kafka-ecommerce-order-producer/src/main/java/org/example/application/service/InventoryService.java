package org.example.application.service;

import com.example.avro.InventoryEvent;
import com.example.avro.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.InventoryController;
import org.example.api.request.InventoryEventRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

@Slf4j
@Service
public class InventoryService {

    private final Sinks.Many<Message<InventoryEvent>> inventorySink = Sinks.many().replay().all();

    @Bean
    public Supplier<Flux<Message<InventoryEvent>>> inventorySupplier() {
        return inventorySink::asFlux;
    }

    public void publishInventoryEvent(InventoryEventRequest request) {
        InventoryEvent event = InventoryEvent.newBuilder()
                .setProductId(request.productId())
                .setStockAvailable(request.stockAvailable())
                .setThreshold(request.threshold())
                .build();

        Message<InventoryEvent> message = MessageBuilder.withPayload(event).build();

        log.info("📤 Publishing event: {}", event);
        inventorySink.tryEmitNext(message).orThrow();
    }
}