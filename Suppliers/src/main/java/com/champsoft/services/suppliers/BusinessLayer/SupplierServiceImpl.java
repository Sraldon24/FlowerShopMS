/* =====================
   SupplierServiceImpl.java
   ===================== */
package com.champsoft.services.suppliers.BusinessLayer;

import com.champsoft.services.suppliers.DataLayer.Supplier;
import com.champsoft.services.suppliers.DataLayer.SupplierRepository;
import com.champsoft.services.suppliers.Mapperlayer.SupplierRequestMapper;
import com.champsoft.services.suppliers.Mapperlayer.SupplierResponseMapper;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.champsoft.services.suppliers.PresentationLayer.SupplierResponseModel;
import com.champsoft.services.suppliers.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierResponseMapper supplierResponseMapper;
    private final SupplierRequestMapper supplierRequestMapper;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               SupplierResponseMapper supplierResponseMapper,
                               SupplierRequestMapper supplierRequestMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierResponseMapper = supplierResponseMapper;
        this.supplierRequestMapper = supplierRequestMapper;
    }

    @Override
    public List<SupplierResponseModel> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return supplierResponseMapper.entityListToResponseModelList(suppliers);
    }

    @Override
    public SupplierResponseModel getSupplierBySupplierId(String supplierId) {
        Supplier supplier = supplierRepository.findSupplierBySupplierIdentifier(supplierId);
        if (supplier == null) {
            throw new NotFoundException("Supplier with ID " + supplierId + " not found.");
        }
        return supplierResponseMapper.entityToResponseModel(supplier);
    }

    @Override
    public SupplierResponseModel addSupplier(SupplierRequestModel newSupplierData) {
        if (newSupplierData.getPassword1() == null || !newSupplierData.getPassword1().equals(newSupplierData.getPassword2())) {
            throw new IllegalArgumentException("Entered passwords do not match!");
        }

        if (supplierRepository.findSupplierBySupplierIdentifier(newSupplierData.getSupplierId()) != null) {
            throw new IllegalArgumentException("Supplier with ID " + newSupplierData.getSupplierId() + " already exists. Choose another Supplier Identifier.");
        }

        Supplier supplier = supplierRequestMapper.requestModelToEntity(newSupplierData);
        supplier.setPassword(newSupplierData.getPassword1());

        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierResponseMapper.entityToResponseModel(savedSupplier);
    }

    @Override
    public SupplierResponseModel updateSupplier(String supplierId, SupplierRequestModel newSupplierData) {
        Supplier foundSupplier = supplierRepository.findSupplierBySupplierIdentifier(supplierId);
        if (foundSupplier == null) {
            throw new NotFoundException("Supplier with ID " + supplierId + " not found.");
        }

        if (newSupplierData.getPassword1() == null || !newSupplierData.getPassword1().equals(newSupplierData.getPassword2())) {
            throw new IllegalArgumentException("Entered passwords do not match!");
        }

        Supplier updatedSupplier = supplierRequestMapper.requestModelToEntity(newSupplierData);
        updatedSupplier.setId(foundSupplier.getId());
        updatedSupplier.setSupplierIdentifier(supplierId);
        updatedSupplier.setPassword(newSupplierData.getPassword1());

        Supplier savedSupplier = supplierRepository.save(updatedSupplier);
        return supplierResponseMapper.entityToResponseModel(savedSupplier);
    }

    @Override
    public String deleteSupplierBySupplierId(String supplierId) {
        Supplier foundSupplier = supplierRepository.findSupplierBySupplierIdentifier(supplierId);
        if (foundSupplier == null) {
            throw new NotFoundException("Supplier with ID " + supplierId + " not found.");
        }

        supplierRepository.delete(foundSupplier);
        return "Supplier with ID " + supplierId + " deleted successfully.";
    }
}
