/* ==============================
   SupplierRequestModel.java
   ============================== */
package com.champsoft.services.suppliers.PresentationLayer;

import com.champsoft.services.suppliers.DataLayer.SupplierPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierRequestModel {

    private String supplierId;
    private String companyName;
    private String contactPerson;
    private String emailAddress;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String province;
    private String username;
    private String password1;
    private String password2;
    private List<SupplierPhoneNumber> phoneNumbers;
}
