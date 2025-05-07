package org.example.Inventory.BusinessLayer.Inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.DomainClientLayer.Inventory.InventoryServiceClient;
import org.example.Inventory.PresentationLayer.Inventory.InventoryController;
import org.example.Inventory.PresentationLayer.Inventory.InventoryRequestModel;
import org.example.Inventory.PresentationLayer.Inventory.InventoryResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl {

    private final InventoryServiceClient inventoryServiceClient;

    public List<InventoryResponseModel> getInventories() {
        List<InventoryResponseModel> inventories = inventoryServiceClient.getInventories();

        inventories.forEach(inv -> {
            inv.add(linkTo(methodOn(InventoryController.class).getInventoryById(inv.getInventoryId())).withRel("get-by-id"));
            inv.add(linkTo(methodOn(InventoryController.class).deleteInventory(inv.getInventoryId())).withRel("delete"));
        });

        return inventories;
    }

    public InventoryResponseModel getInventoryById(String inventoryId) {
        return inventoryServiceClient.getInventoryById(inventoryId);
    }

    public InventoryResponseModel addInventory(InventoryRequestModel requestModel) {
        return inventoryServiceClient.addInventory(requestModel);
    }

    public InventoryResponseModel updateInventory(String id, InventoryRequestModel requestModel) {
        return inventoryServiceClient.updateInventory(id, requestModel);
    }

    public String deleteInventory(String id) {
        return inventoryServiceClient.deleteInventory(id);
    }
}
