/* ==========================
   SupplierRepository.java
   ========================== */
package com.champsoft.services.suppliers.DataLayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findSupplierBySupplierIdentifier(String supplierIdentifier);

    Supplier findSupplierByEmailAddress(String emailAddress);
}
