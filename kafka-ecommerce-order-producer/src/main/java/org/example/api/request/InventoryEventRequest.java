package org.example.api.request;

public record InventoryEventRequest(
        String productId,
        Integer stockAvailable,
        Integer threshold

) {
}
