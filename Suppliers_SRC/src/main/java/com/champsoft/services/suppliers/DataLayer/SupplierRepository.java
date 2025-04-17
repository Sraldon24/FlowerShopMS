package com.champsoft.services.suppliers.DataLayer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {
    Supplier findSupplierBySupplierIdentifier(String supplierIdentifier);
    Supplier findSupplierByEmailAddress(String emailAddress);
}
