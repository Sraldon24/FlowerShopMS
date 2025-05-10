package org.example.Inventory.BusinessLayer.InventoryFlower;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.DomainClientLayer.InventoryFlower.InventoryFlowerServiceClient;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryFlowerServiceImpl {

    private final InventoryFlowerServiceClient inventoryFlowerServiceClient;

    public List<FlowerResponseModel> getFlowersInInventory(String inventoryId) {
        return inventoryFlowerServiceClient.getFlowersInInventory(inventoryId);
    }

    public FlowerResponseModel getInventoryFlowerById(String inventoryId, String flowerId) {
        return inventoryFlowerServiceClient.getFlowerInInventoryById(inventoryId, flowerId);
    }
}
