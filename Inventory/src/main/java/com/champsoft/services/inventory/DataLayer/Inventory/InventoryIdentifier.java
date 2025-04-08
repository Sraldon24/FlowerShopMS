/* ===========================
   InventoryIdentifier.java
   =========================== */
package com.champsoft.services.inventory.DataLayer.Inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryIdentifier {

    @Column(name = "inventory_identifier", unique = true, nullable = false)
    private String inventoryId;

    public void generateIfMissing() {
        if (this.inventoryId == null || this.inventoryId.isEmpty()) {
            this.inventoryId = UUID.randomUUID().toString();
        }
    }
}
