/* ================================
   InventoryFlowerController.java
   ================================ */
package com.champsoft.services.inventory.PresentationLayer.Inventory_Flower;

import com.champsoft.services.inventory.BusinessLayer.Inventory_Flower.InventoryFlowerService;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories/{inventoryId}/flowers")
public class InventoryFlowerController {

    private final InventoryFlowerService inventoryFlowerService;

    public InventoryFlowerController(InventoryFlowerService inventoryFlowerService) {
        this.inventoryFlowerService = inventoryFlowerService;
    }

    @GetMapping
    public ResponseEntity<List<FlowerResponseModel>> getFlowersInInventory(@PathVariable String inventoryId) {
        return ResponseEntity.ok().body(inventoryFlowerService.getFlowersInInventory(inventoryId));
    }

    @GetMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> getFlowerById(
            @PathVariable String inventoryId,
            @PathVariable String flowerId
    ) {
        return ResponseEntity.ok().body(inventoryFlowerService.getInventoryFlowerById(inventoryId, flowerId));
    }

    @PostMapping
    public ResponseEntity<FlowerResponseModel> addFlower(
            @PathVariable String inventoryId,
            @RequestBody FlowerRequestModel flowerRequestModel
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryFlowerService.addFlowerToInventory(inventoryId, flowerRequestModel));
    }

    @PutMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> updateFlower(
            @PathVariable String inventoryId,
            @PathVariable String flowerId,
            @RequestBody FlowerRequestModel flowerRequestModel
    ) {
        return ResponseEntity.ok()
                .body(inventoryFlowerService.updateFlowerInInventory(inventoryId, flowerId, flowerRequestModel));
    }

    @DeleteMapping("/{flowerId}")
    public ResponseEntity<Void> removeFlowerFromInventory(
            @PathVariable String inventoryId,
            @PathVariable String flowerId
    ) {
        inventoryFlowerService.removeFlowerFromInventory(inventoryId, flowerId);
        return ResponseEntity.noContent().build();
    }
}
