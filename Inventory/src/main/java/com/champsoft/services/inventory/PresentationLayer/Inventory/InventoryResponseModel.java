/* ================================
   InventoryResponseModel.java
   ================================ */
package com.champsoft.services.inventory.PresentationLayer.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseModel {
    private String inventoryId;
    private String type;
}
