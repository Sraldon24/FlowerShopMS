package com.champsoft.services.inventory.MapperLayer;

import com.champsoft.services.inventory.DataLayer.Flowers.Price;
import com.champsoft.services.inventory.utils.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    @Named("priceToBigDecimal")
    default BigDecimal priceToBigDecimal(Price price) {
        return (price != null) ? price.getAmount() : null;
    }

    @Named("bigDecimalToPrice")
    default Price bigDecimalToPrice(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return Price.builder()
                .amount(amount)
                .currency(Currency.CAD) // ✅ Ensure this is the correct currency enum
                .build();
    }
}
