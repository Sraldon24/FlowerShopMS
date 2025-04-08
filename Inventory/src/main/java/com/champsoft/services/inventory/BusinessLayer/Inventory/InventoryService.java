package com.champsoft.services.inventory.BusinessLayer.Inventory;

import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryResponseModel;

import java.util.List;

public interface InventoryService {
    List<InventoryResponseModel> getInventories();

    InventoryResponseModel getInventoryById(String inventoryId);

    InventoryResponseModel addInventory(InventoryRequestModel inventoryRequestModel);

    InventoryResponseModel updateInventoryDetails(InventoryRequestModel inventoryRequestModel, String inventoryId);

    void deleteInventory(String inventoryId);
}
