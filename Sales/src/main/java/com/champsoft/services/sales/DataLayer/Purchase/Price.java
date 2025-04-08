package com.champsoft.services.sales.DataLayer.Purchase;


import com.champsoft.services.utils.Currency;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;         // e.g., "CAD"
}

