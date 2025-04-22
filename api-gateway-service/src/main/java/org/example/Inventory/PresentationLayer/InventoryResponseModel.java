package org.example.Inventory.PresentationLayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryResponseModel extends RepresentationModel<InventoryResponseModel> {
    private String inventoryId;
    private String type;
}
