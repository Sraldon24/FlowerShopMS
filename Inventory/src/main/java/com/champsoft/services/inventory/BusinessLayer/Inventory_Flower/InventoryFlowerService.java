/* =====================================
   InventoryFlowerService.java
   ===================================== */
package com.champsoft.services.inventory.BusinessLayer.Inventory_Flower;

import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;

import java.util.List;

public interface InventoryFlowerService {
    List<FlowerResponseModel> getFlowersInInventory(String inventoryId);

    FlowerResponseModel getInventoryFlowerById(String inventoryId, String flowerId);

    FlowerResponseModel addFlowerToInventory(String inventoryId, FlowerRequestModel flowerRequestModel);

    FlowerResponseModel updateFlowerInInventory(String inventoryId, String flowerId, FlowerRequestModel flowerRequestModel);

    void removeFlowerFromInventory(String inventoryId, String flowerId);
}
