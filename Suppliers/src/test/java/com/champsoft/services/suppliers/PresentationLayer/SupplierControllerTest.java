package com.champsoft.services.suppliers.PresentationLayer;

import com.champsoft.services.suppliers.BusinessLayer.SupplierServiceImpl;
import com.champsoft.services.suppliers.utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SupplierControllerTest {

    @Mock
    private SupplierServiceImpl supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierRequestModel validSupplier;
    private SupplierResponseModel responseSupplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validSupplier = new SupplierRequestModel(
                "sup-0001", "Test Company", "Test Person", "test@example.com", "123 Test St", "H1H 1H1",
                "Test City", "Test Province", "testuser", "secure123", "secure123", null);

        responseSupplier = new SupplierResponseModel(
                "sup-0001", "Test Company", "Test Person", "test@example.com", "123 Test St", "H1H 1H1",
                "Test City", "Test Province", null);
    }

    @Test
    void testGetAllSuppliers() {
        // Mock data
        when(supplierService.getSuppliers()).thenReturn(Arrays.asList(responseSupplier));

        // Call the updated method
        ResponseEntity<CollectionModel<SupplierResponseModel>> response = supplierController.getSuppliers();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CollectionModel<SupplierResponseModel> model = response.getBody();
        assertNotNull(model);

        List<SupplierResponseModel> content = new ArrayList<>(model.getContent());
        assertEquals(1, content.size());
        assertEquals("sup-0001", content.get(0).getSupplierId());
    }

    @Test
    void testGetSupplierById_valid() {
        when(supplierService.getSupplierBySupplierId("sup-0001")).thenReturn(responseSupplier);

        ResponseEntity<SupplierResponseModel> response = supplierController.getSupplierBySupplierId("sup-0001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sup-0001", response.getBody().getSupplierId());
    }

    @Test
    void testGetSupplierById_notFound() {
        when(supplierService.getSupplierBySupplierId("invalid")).thenThrow(new NotFoundException("Supplier not found"));

        assertThrows(NotFoundException.class, () -> supplierController.getSupplierBySupplierId("invalid"));
    }

    @Test
    void testAddSupplier_valid() {
        when(supplierService.addSupplier(any(SupplierRequestModel.class))).thenReturn(responseSupplier);

        ResponseEntity<SupplierResponseModel> response = supplierController.addSupplier(validSupplier);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("sup-0001", response.getBody().getSupplierId());
    }

    @Test
    void testUpdateSupplier_valid() {
        when(supplierService.updateSupplier(eq("sup-0001"), any(SupplierRequestModel.class))).thenReturn(responseSupplier);

        ResponseEntity<SupplierResponseModel> response = supplierController.updateSupplier("sup-0001", validSupplier);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("sup-0001", response.getBody().getSupplierId());
    }

    @Test
    void testDeleteSupplier_valid() {
        when(supplierService.deleteSupplierBySupplierId("sup-0001")).thenReturn("Supplier with ID sup-0001 deleted successfully.");

        ResponseEntity<String> response = supplierController.deleteSupplierBySupplierId("sup-0001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("deleted"));
    }

    @Test
    void testDeleteSupplier_notFound() {
        when(supplierService.deleteSupplierBySupplierId("unknown")).thenThrow(new NotFoundException("Supplier not found"));

        assertThrows(NotFoundException.class, () -> supplierController.deleteSupplierBySupplierId("unknown"));
    }
}
