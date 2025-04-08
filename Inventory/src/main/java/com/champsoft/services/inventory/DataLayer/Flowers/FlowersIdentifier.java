/* =========================
   FlowersIdentifier.java
   ========================= */
package com.champsoft.services.inventory.DataLayer.Flowers;

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
public class FlowersIdentifier {

    @Column(name = "flower_identifier")
    private String flowerNumber;

    public void generateIfMissing() {
        if (this.flowerNumber == null || this.flowerNumber.isEmpty()) {
            // Prefix can stay as "VIN-" or you can change it to "FLOWER-"
            this.flowerNumber = "flw-" + UUID.randomUUID().toString().substring(0, 13);
        }
    }
}
