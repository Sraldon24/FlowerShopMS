/* ================================
   InventoryResponseModel.java
   ================================ */
package com.champsoft.services.inventory.PresentationLayer.Inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseModel extends RepresentationModel<InventoryResponseModel> {
    private String inventoryId;
    private String type;
}
