/* =========================
   SupplierController.java
   ========================= */
package com.champsoft.services.suppliers.PresentationLayer;

import com.champsoft.services.suppliers.BusinessLayer.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierServiceImpl supplierService;

    @Autowired
    public SupplierController(SupplierServiceImpl supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<SupplierResponseModel>> getSuppliers() {
        List<SupplierResponseModel> suppliers = supplierService.getSuppliers();

        for (SupplierResponseModel supplier : suppliers) {
            String supplierId = supplier.getSupplierId();

            supplier.add(linkTo(methodOn(SupplierController.class)
                    .getSupplierBySupplierId(supplierId)).withRel("get-supplier"));

            supplier.add(linkTo(methodOn(SupplierController.class)
                    .deleteSupplierBySupplierId(supplierId)).withRel("delete-supplier"));
        }

        // Optional: Add a self link to the entire collection
        CollectionModel<SupplierResponseModel> model = CollectionModel.of(suppliers,
                linkTo(methodOn(SupplierController.class).getSuppliers()).withSelfRel());

        return ResponseEntity.ok(model);
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
