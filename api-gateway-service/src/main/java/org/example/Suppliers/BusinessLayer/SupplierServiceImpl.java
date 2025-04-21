package org.example.Suppliers.BusinessLayer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Suppliers.DomainClientLayer.SupplierServiceClient;
import org.example.Suppliers.PresentationLayer.SupplierRequestModel;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl {

    private final SupplierServiceClient supplierServiceClient;

    public List<SupplierResponseModel> getSuppliers() {
        return supplierServiceClient.getSuppliers();
    }

    public SupplierResponseModel getSupplierBySupplierId(String supplierId) {
        return supplierServiceClient.getSupplierById(supplierId);
    }

    public SupplierResponseModel addSupplier(SupplierRequestModel newSupplierData) {
        return supplierServiceClient.addSupplier(newSupplierData);
    }

    public SupplierResponseModel updateSupplier(String supplierId, SupplierRequestModel newSupplierData) {
        return supplierServiceClient.updateSupplier(supplierId, newSupplierData);
    }

    public String deleteSupplierBySupplierId(String supplierId) {
        return supplierServiceClient.deleteSupplier(supplierId);
    }
}
