/* =====================================
   InventoryFlowerResponseModel.java
   ===================================== */
package com.champsoft.services.inventory.PresentationLayer.Inventory_Flower;

import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryFlowerResponseModel {
    private String inventoryId;
    private String type;
    private List<FlowerResponseModel> availableFlowers;
}
