package org.example.api.request;

public record OrderCreatedRequest(
        String orderId,
        String customerId,
        String productId,
        Integer quantity,
        Double orderAmount,
        String orderTimestamp,
        Status status
) {
    public enum Status {
        PENDING,
        COMPLETED,
        FAILED
    }
}
