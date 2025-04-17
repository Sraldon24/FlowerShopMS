package com.champsoft.services.suppliers.DataLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
