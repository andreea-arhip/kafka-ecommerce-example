package org.example.application.consumer;

import com.example.avro.InventoryEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Slf4j
@Getter
@Component
public class InventoryListener {

    @Bean
    public Consumer<Message<InventoryEvent>> readInventory() {
        return message -> {
            InventoryEvent event = message.getPayload();

            log.info("✅ Received Inventory Event: Product ID: {}, Stock: {}, Threshold: {}",
                    event.getProductId(), event.getStockAvailable(), event.getThreshold());
        };
    }
}
