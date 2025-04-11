package com.champsoft.services.suppliers.DataLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class SupplierPhoneNumber {

    private PhoneType type;
    private String number;
}
