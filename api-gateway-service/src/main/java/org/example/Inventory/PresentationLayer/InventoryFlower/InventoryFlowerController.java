package org.example.Inventory.PresentationLayer.InventoryFlower;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.BusinessLayer.InventoryFlower.InventoryFlowerServiceImpl;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories/{inventoryId}/flowers")
@RequiredArgsConstructor
@Slf4j
public class InventoryFlowerController {

    private final InventoryFlowerServiceImpl inventoryFlowerService;

    @GetMapping
    public ResponseEntity<List<FlowerResponseModel>> getFlowersInInventory(@PathVariable String inventoryId) {
        return ResponseEntity.ok(inventoryFlowerService.getFlowersInInventory(inventoryId));
    }

    @GetMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> getInventoryFlowerById(
            @PathVariable String inventoryId,
            @PathVariable String flowerId
    ) {
        return ResponseEntity.ok(inventoryFlowerService.getInventoryFlowerById(inventoryId, flowerId));
    }
}
