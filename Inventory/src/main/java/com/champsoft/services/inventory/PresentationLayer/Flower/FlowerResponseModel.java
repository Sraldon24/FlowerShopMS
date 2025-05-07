/* ===========================
   FlowerResponseModel.java
   =========================== */
package com.champsoft.services.inventory.PresentationLayer.Flower;

import com.champsoft.services.inventory.PresentationLayer.OptionDto;
import com.champsoft.services.inventory.PresentationLayer.supplierdtos.SupplierResponseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerResponseModel extends RepresentationModel<FlowerResponseModel> {
    private String flowerId; // This maps to flower_identifier in the database
    private String inventoryId; // Matches inventory_id
    private String flowerName; // Matches flower_name
    private String flowerColor; // Matches flower_color
    private String flowerCategory; // Matches flower_category
    private String flowerStatus; // Matches flower_status
    private int stockQuantity; // Matches stock_quantity
    private String supplierIdentifier; // Matches supplier_id
    private BigDecimal price; // Matches price_amount
    private String currency; // Matches price_currency
    private List<OptionDto> options; // Matches flower_options
    private SupplierResponseModel supplier;
}
