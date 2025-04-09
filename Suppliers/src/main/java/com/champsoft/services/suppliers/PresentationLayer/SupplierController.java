/* =========================
   SupplierController.java
   ========================= */
package com.champsoft.services.suppliers.PresentationLayer;

import com.champsoft.services.suppliers.BusinessLayer.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierServiceImpl supplierService;

    @Autowired
    public SupplierController(SupplierServiceImpl supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseModel>> getSuppliers() {
        return ResponseEntity.ok().body(this.supplierService.getSuppliers());
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseModel> getSupplierBySupplierId(@PathVariable String supplierId) {
        return ResponseEntity.ok().body(this.supplierService.getSupplierBySupplierId(supplierId));
    }

    @PostMapping
    public ResponseEntity<SupplierResponseModel> addSupplier(@RequestBody SupplierRequestModel newSupplierData) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.supplierService.addSupplier(newSupplierData));
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseModel> updateSupplier(@PathVariable String supplierId,
                                                                @RequestBody SupplierRequestModel newSupplierData) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.supplierService.updateSupplier(supplierId, newSupplierData));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<String> deleteSupplierBySupplierId(@PathVariable String supplierId) {
        String message = this.supplierService.deleteSupplierBySupplierId(supplierId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
