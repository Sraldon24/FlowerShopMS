package org.example.Suppliers.PresentationLayer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Suppliers.BusinessLayer.SupplierServiceImpl;
import org.example.Suppliers.DomainClientLayer.SupplierServiceClient;
import org.example.Suppliers.Utils.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierController {

    private final SupplierServiceImpl supplierService;
    private final SupplierServiceClient supplierServiceClient;
    private static final int UUID_SIZE = 36;

    @GetMapping
    public ResponseEntity<List<SupplierResponseModel>> getSuppliers() {
        return ResponseEntity.ok(supplierService.getSuppliers());
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseModel> getSupplierBySupplierId(@PathVariable String supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierBySupplierId(supplierId));
    }

    @PostMapping
    public ResponseEntity<SupplierResponseModel> addSupplier(@RequestBody SupplierRequestModel newSupplierData) {
        SupplierResponseModel responseModel = supplierServiceClient.addSupplier(newSupplierData);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseModel> updateSupplier(@PathVariable String supplierId,
                                                                @RequestBody SupplierRequestModel newSupplierData) {
        if (supplierId.length() != UUID_SIZE)
            throw new InvalidInputException("Invalid supplier ID");

        return ResponseEntity.ok(supplierService.updateSupplier(supplierId, newSupplierData));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<String> deleteSupplierBySupplierId(@PathVariable String supplierId) {
        return ResponseEntity.ok(supplierService.deleteSupplierBySupplierId(supplierId));
    }
}
