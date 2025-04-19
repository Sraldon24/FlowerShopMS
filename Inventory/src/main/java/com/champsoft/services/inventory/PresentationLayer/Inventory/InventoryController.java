/* ===========================
   InventoryController.java
   =========================== */
package com.champsoft.services.inventory.PresentationLayer.Inventory;

import com.champsoft.services.inventory.BusinessLayer.Inventory.InventoryService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<InventoryResponseModel>> getInventories() {
        List<InventoryResponseModel> inventoryList = inventoryService.getInventories();

        return ResponseEntity.ok(
                CollectionModel.of(inventoryList,
                        linkTo(methodOn(InventoryController.class).getInventories()).withSelfRel())
        );
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseModel> getInventoryById(@PathVariable String inventoryId) {
        return ResponseEntity.ok().body(inventoryService.getInventoryById(inventoryId));
    }

    @PostMapping
    public ResponseEntity<InventoryResponseModel> addInventory(@RequestBody InventoryRequestModel inventoryRequestModel) {
        return ResponseEntity.ok().body(inventoryService.addInventory(inventoryRequestModel));
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseModel> updateInventory(@PathVariable String inventoryId,
                                                                  @RequestBody InventoryRequestModel inventoryRequestModel) {
        return ResponseEntity.ok().body(inventoryService.updateInventoryDetails(inventoryRequestModel, inventoryId));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }
}
