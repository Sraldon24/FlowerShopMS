/* =============
   Address.java
   ============= */
package com.champsoft.services.suppliers.DataLayer;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class Address {
    private String streetAddress;
    private String postalCode;
    private String city;
    private String province;
}
