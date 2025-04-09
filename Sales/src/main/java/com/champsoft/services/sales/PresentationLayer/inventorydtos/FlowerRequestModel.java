/* ===========================
   FlowerRequestModel.java
   =========================== */
package com.champsoft.services.sales.PresentationLayer.inventorydtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerRequestModel {
    private String flowerId;
    private String inventoryId;

    private String flowerName; // Added to match Flower entity
    private String flowerColor; // Added to match Flower entity
    private String flowerCategory; // Added to match Flower entity

    private String flowerStatus; // Status is an ENUM in entity
    private Integer stockQuantity; // Matches stock quantity in entity
    private String supplierIdentifier; // Matches supplierId in entity

    private BigDecimal price;
    private String currency;

    private List<OptionDto> options;
}
