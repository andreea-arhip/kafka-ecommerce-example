package org.example.api.request;

public record OrderCreatedRequest(
        String orderId,
        String customerId,
        Integer orderAmount,
        String orderTimestamp,
        Status status
) {
    public enum Status {
        PENDING,
        COMPLETED,
        FAILED
    }
}
