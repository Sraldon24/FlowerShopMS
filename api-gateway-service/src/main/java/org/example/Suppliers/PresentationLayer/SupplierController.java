package org.example.Suppliers.PresentationLayer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Suppliers.BusinessLayer.SupplierServiceImpl;
import org.example.Suppliers.Utils.InvalidInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierController {

    private final SupplierServiceImpl supplierService;
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
        return ResponseEntity.status(201).body(supplierService.addSupplier(newSupplierData));
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
