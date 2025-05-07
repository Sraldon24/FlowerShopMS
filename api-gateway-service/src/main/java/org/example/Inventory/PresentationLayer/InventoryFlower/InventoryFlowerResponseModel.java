package org.example.Inventory.PresentationLayer.InventoryFlower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryFlowerResponseModel  {
    private String inventoryId;
    private String type;
    private List<FlowerResponseModel> availableFlowers;
}
