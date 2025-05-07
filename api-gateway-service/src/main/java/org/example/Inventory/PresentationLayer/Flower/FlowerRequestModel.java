package org.example.Inventory.PresentationLayer.Flower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerRequestModel {
    private String flowerId;
    private String inventoryId;
    private String flowerName;
    private String flowerColor;
    private String flowerCategory;
    private String flowerStatus;
    private int stockQuantity;
    private String supplierIdentifier;
    private BigDecimal price;
    private String currency;
    private List<OptionDto> options;
}
