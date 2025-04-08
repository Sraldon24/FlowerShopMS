package com.champsoft.services.sales.DataLayer.Identifiers;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierIdentifier {
    private String supplierId;
}
