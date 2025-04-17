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
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierResponseMapper supplierResponseMapper;
    private final SupplierRequestMapper supplierRequestMapper;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierRequestMapper requestMapper, SupplierResponseMapper responseMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierRequestMapper = requestMapper;
        this.supplierResponseMapper = responseMapper;
    }

    @Override
    public List<SupplierResponseModel> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        if (suppliers == null || suppliers.isEmpty()) {
            return List.of();
        }
        return suppliers.stream()
                .map(supplierResponseMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierResponseModel getSupplierBySupplierId(String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        Supplier supplier = supplierRepository.findSupplierBySupplierIdentifier(supplierId);
        if (supplier == null) {
            throw new NotFoundException("Supplier not found");
        }
        return supplierResponseMapper.entityToResponseModel(supplier);
    }


    @Override
    public SupplierResponseModel addSupplier(SupplierRequestModel requestModel) {
        if (requestModel == null) {
            throw new IllegalArgumentException("Request model cannot be null");
        }
        if (!requestModel.getPassword1().equals(requestModel.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (supplierRepository.findSupplierBySupplierIdentifier(requestModel.getSupplierId()) != null) {
            throw new IllegalArgumentException("Supplier with this ID already exists");
        }
        Supplier supplier = supplierRequestMapper.requestModelToEntity(requestModel);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierResponseMapper.entityToResponseModel(savedSupplier);
    }
    @Override
    public SupplierResponseModel updateSupplier(String supplierId, SupplierRequestModel requestModel) {
        if (requestModel == null) {
            throw new IllegalArgumentException("Request model cannot be null");
        }
        if (!requestModel.getPassword1().equals(requestModel.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        Supplier existingSupplier = supplierRepository.findSupplierBySupplierIdentifier(supplierId);
        if (existingSupplier == null) {
            throw new NotFoundException("Supplier not found");
        }
        if (supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        Supplier updatedSupplier = supplierRequestMapper.requestModelToEntity(requestModel);
        updatedSupplier.setId(existingSupplier.getId());
        Supplier savedSupplier = supplierRepository.save(updatedSupplier);
        return supplierResponseMapper.entityToResponseModel(savedSupplier);
    }


    @Override
    public String deleteSupplierBySupplierId(String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        Supplier supplier = supplierRepository.findSupplierBySupplierIdentifier(supplierId);
        if (supplier == null) {
            throw new NotFoundException("Supplier not found");
        }
        supplierRepository.delete(supplier);
        return "Supplier with ID " + supplierId + " deleted successfully.";
    }
}
