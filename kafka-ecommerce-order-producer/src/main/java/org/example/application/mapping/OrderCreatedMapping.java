package org.example.application.mapping;

import com.example.avro.OrderCreatedEvent;
import com.example.avro.Status;
import org.example.api.request.OrderCreatedRequest;
import org.example.application.exception.StatusMappingException;

import java.util.Optional;

public class OrderCreatedMapping {

    public static OrderCreatedEvent mapOrderCreatedEvent(OrderCreatedRequest request) {
        return OrderCreatedEvent.newBuilder()
                .setOrderId(request.orderId())
                .setCustomerId(request.customerId())
                .setProductId(request.productId())
                .setQuantity(request.quantity())
                .setOrderAmount(request.orderAmount())
                .setOrderTimestamp(request.orderTimestamp())
                .setStatus(mapOrderCreatedStatus(request.status()))
                .build();
    }

    public static Status mapOrderCreatedStatus(OrderCreatedRequest.Status requestStatus) {
        return Optional.ofNullable(requestStatus)
                .map(status -> {
                    try {
                        return com.example.avro.Status.valueOf(status.name());
                    } catch (IllegalArgumentException e) {
                        throw new StatusMappingException("Invalid payment status: " + status);
                    }
                })
                .orElseThrow(() -> new StatusMappingException("Payment status cannot be null"));
    }
}
