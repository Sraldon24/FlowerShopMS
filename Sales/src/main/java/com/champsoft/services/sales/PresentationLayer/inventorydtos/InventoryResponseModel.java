/* ================================
   InventoryResponseModel.java
   ================================ */
package com.champsoft.services.sales.PresentationLayer.inventorydtos;

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
