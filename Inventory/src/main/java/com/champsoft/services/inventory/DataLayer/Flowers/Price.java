package com.champsoft.services.inventory.DataLayer.Flowers;

import com.champsoft.services.inventory.utils.Currency;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@Builder // ✅ Ensure this annotation is present!
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency; // ✅ Use only price_currency
}
