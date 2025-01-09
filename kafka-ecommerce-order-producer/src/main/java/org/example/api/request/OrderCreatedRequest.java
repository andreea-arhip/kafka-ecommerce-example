package org.example.api.request;

public record OrderCreatedRequest(
        String orderId,
        String customerId,
        String productId,
        Integer quantity,
        Float totalPrice,
        Status paymentStatus
) {
    public enum Status {
        PENDING,
        COMPLETED,
        FAILED
    }
}
