/* =============
   OptionDto.java
   ============= */
package com.champsoft.services.sales.PresentationLayer.inventorydtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDto {
    private String name;
    private String description;
    private BigDecimal price;
}
