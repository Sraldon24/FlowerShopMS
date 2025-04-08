/* =============
   Supplier.java
   ============= */
package com.champsoft.services.suppliers.DataLayer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "suppliers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "supplier_identifier", unique = true, nullable = false)
    private String supplierIdentifier;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "email_address")
    private String emailAddress;

    private String username;
    private String password;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "supplier_phonenumbers",
            joinColumns = @JoinColumn(name = "supplier_id")
    )
    private List<SupplierPhoneNumber> phoneNumbers;

    @PrePersist
    public void prePersist() {
        if (this.supplierIdentifier == null || this.supplierIdentifier.isEmpty()) {
            this.supplierIdentifier = java.util.UUID.randomUUID().toString();
        }
    }
}
