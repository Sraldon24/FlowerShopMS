package org.example.Inventory.PresentationLayer.Inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.BusinessLayer.Inventory.InventoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryServiceImpl inventoryService;
    private static final int UUID_SIZE = 36;

    @GetMapping
    public ResponseEntity<List<InventoryResponseModel>> getInventories() {
        return ResponseEntity.ok(inventoryService.getInventories());
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseModel> getInventoryById(@PathVariable String inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @PostMapping
    public ResponseEntity<InventoryResponseModel> addInventory(@RequestBody InventoryRequestModel model) {
        return ResponseEntity.status(201).body(inventoryService.addInventory(model));
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseModel> updateInventory(@PathVariable String inventoryId,
                                                                  @RequestBody InventoryRequestModel model) {
        return ResponseEntity.ok(inventoryService.updateInventory(inventoryId, model));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteInventory(@PathVariable String inventoryId) {
        return ResponseEntity.ok(inventoryService.deleteInventory(inventoryId));
    }
}
