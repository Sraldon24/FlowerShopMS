/* ==============================
   SupplierResponseModel.java
   ============================== */
package com.champsoft.services.suppliers.PresentationLayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierResponseModel extends RepresentationModel<SupplierResponseModel> {

    private String supplierId;
    private String companyName;
    private String contactPerson;
    private String emailAddress;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String province;
    private List<PhoneNumberDTO> phoneNumbers;
}
