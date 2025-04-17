package com.champsoft.services.suppliers.BusinessLayer;

import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.champsoft.services.suppliers.PresentationLayer.SupplierResponseModel;

import java.util.List;

public interface SupplierService {
    List<SupplierResponseModel> getSuppliers();

    SupplierResponseModel getSupplierBySupplierId(String supplierId);

    SupplierResponseModel addSupplier(SupplierRequestModel newSupplierData);

    SupplierResponseModel updateSupplier(String supplierId, SupplierRequestModel newSupplierData);

    String deleteSupplierBySupplierId(String supplierId);
}
