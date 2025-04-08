/* ================
   inventory.java
   ================ */
package com.champsoft.services.inventory.DataLayer.Inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventories")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private InventoryIdentifier inventoryIdentifier;

    private String type;

    @PrePersist
    public void prePersist() {
        if (this.inventoryIdentifier == null) {
            this.inventoryIdentifier = new InventoryIdentifier();
        }
        this.inventoryIdentifier.generateIfMissing();
    }
}
