package com.champsoft.services.suppliers.DataLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "suppliers") // MongoDB collection name
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Supplier {

    @Id
    private String id;  // MongoDB uses String IDs (Hex ObjectIds or UUIDs)

    private String supplierIdentifier;
    private String companyName;
    private String contactPerson;
    private String emailAddress;
    private String username;
    private String password;

    private Address address;
    private List<SupplierPhoneNumber> phoneNumbers;

    public void generateIdentifierIfMissing() {
        if (this.supplierIdentifier == null || this.supplierIdentifier.isEmpty()) {
            this.supplierIdentifier = java.util.UUID.randomUUID().toString();
        }
    }
}
