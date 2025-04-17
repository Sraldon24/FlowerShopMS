package com.champsoft.services.suppliers.PresentationLayer;

import com.champsoft.services.suppliers.BusinessLayer.SupplierServiceImpl;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.champsoft.services.suppliers.PresentationLayer.SupplierResponseModel;
import com.champsoft.services.suppliers.utils.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierServiceImpl supplierService;

    public SupplierController(SupplierServiceImpl supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseModel>> getSuppliers() {
        List<SupplierResponseModel> suppliers = supplierService.getSuppliers();
        if (suppliers == null) {
            suppliers = List.of(); // Return an empty list if the service returns null
        }
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseModel> getSupplierBySupplierId(@PathVariable String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        SupplierResponseModel supplier = supplierService.getSupplierBySupplierId(supplierId);
        return new ResponseEntity<>(supplier, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SupplierResponseModel> addSupplier(@RequestBody SupplierRequestModel requestModel) {
        if (requestModel == null) {
            throw new IllegalArgumentException("Request model cannot be null");
        }
        if (!requestModel.getPassword1().equals(requestModel.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        SupplierResponseModel supplier = supplierService.addSupplier(requestModel);
        return new ResponseEntity<>(supplier, HttpStatus.CREATED);
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseModel> updateSupplier(String supplierId, SupplierRequestModel requestModel) {
        if (supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        if (requestModel == null) {
            throw new IllegalArgumentException("Request model cannot be null");
        }
        if (!requestModel.getPassword1().equals(requestModel.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        SupplierResponseModel supplier = supplierService.updateSupplier(supplierId, requestModel);
        return new ResponseEntity<>(supplier, HttpStatus.CREATED);
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<String> deleteSupplierBySupplierId(@PathVariable String supplierId) {
        if (supplierId == null || supplierId.isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be null or empty");
        }
        String message = supplierService.deleteSupplierBySupplierId(supplierId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}