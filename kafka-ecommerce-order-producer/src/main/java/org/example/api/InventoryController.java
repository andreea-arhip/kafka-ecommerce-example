package org.example.api;

import lombok.RequiredArgsConstructor;
import org.example.api.request.InventoryEventRequest;
import org.example.application.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public void publishInventory(@RequestBody InventoryEventRequest inventoryEventRequest) {
        inventoryService.publishInventoryEvent(inventoryEventRequest);
    }
}
