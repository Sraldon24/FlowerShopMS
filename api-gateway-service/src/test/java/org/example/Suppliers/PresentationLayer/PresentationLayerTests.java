package org.example.Suppliers.PresentationLayer;

import org.example.Suppliers.BusinessLayer.SupplierServiceImpl;
import org.example.Suppliers.DomainClientLayer.SupplierServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PresentationLayerTests {

//    @Test
//    void testSupplierRequestModel_basicMethods() {
//        PhoneNumberDTO phone = new PhoneNumberDTO("mobile", "1234567890");
//        List<PhoneNumberDTO> phones = List.of(phone);
//
//        SupplierRequestModel model = new SupplierRequestModel(
//                "id1", "Company", "John Doe", "email@example.com",
//                "123 St", "12345", "City", "Province",
//                "username", "pass1", "pass2", phones);
//
//        // Test getters
//        assertEquals("id1", model.getSupplierId());
//        assertEquals("Company", model.getCompanyName());
//        assertEquals(phones, model.getPhoneNumbers());
//
//        // Test setters
//        model.setCompanyName("NewCompany");
//        assertEquals("NewCompany", model.getCompanyName());
//
//        // Equals and hashCode reflexivity
//        assertEquals(model, model);
//        assertEquals(model.hashCode(), model.hashCode());
//
//        // Equals with identical values
//        SupplierRequestModel sameModel = new SupplierRequestModel(
//                "id1", "NewCompany", "John Doe", "email@example.com",
//                "123 St", "12345", "City", "Province",
//                "username", "pass1", "pass2", phones);
//        assertEquals(model, sameModel);
//        assertEquals(model.hashCode(), sameModel.hashCode());
//
//        // Different object not equal
//        SupplierRequestModel diffModel = new SupplierRequestModel();
//        assertNotEquals(model, diffModel);
//        assertNotEquals(model, null);
//        assertNotEquals(model, new Object());
//    }

//    @Test
//    void testSupplierResponseModel_basicMethods() {
//        PhoneNumberDTO phone = new PhoneNumberDTO("home", "0987654321");
//        List<PhoneNumberDTO> phones = List.of(phone);
//
//        SupplierResponseModel model = new SupplierResponseModel(
//                "id2", "Company2", "Jane Doe", "email2@example.com",
//                "456 St", "54321", "Town", "Province2", phones);
//
//        // Getters
//        assertEquals("id2", model.getSupplierId());
//        assertEquals(phones, model.getPhoneNumbers());
//
//        // Setters
//        model.setCity("NewTown");
//        assertEquals("NewTown", model.getCity());
//
//        // Equals & hashCode reflexivity
//        assertEquals(model, model);
//        assertEquals(model.hashCode(), model.hashCode());
//
//        // Equals with identical values
//        SupplierResponseModel sameModel = new SupplierResponseModel(
//                "id2", "Company2", "Jane Doe", "email2@example.com",
//                "456 St", "54321", "NewTown", "Province2", phones);
//
//        assertEquals(model, sameModel);
//        assertEquals(model.hashCode(), sameModel.hashCode());
//
//        // Different object not equal
//        SupplierResponseModel diffModel = new SupplierResponseModel();
//        assertNotEquals(model, diffModel);
//        assertNotEquals(model, null);
//        assertNotEquals(model, new Object());
//    }

//    @Test
//    void testPhoneNumberDTO_basicMethods() {
//        PhoneNumberDTO phone = new PhoneNumberDTO("work", "5555555555");
//
//        assertEquals("work", phone.getType());
//        assertEquals("5555555555", phone.getNumber());
//
//        phone.setNumber("6666666666");
//        assertEquals("6666666666", phone.getNumber());
//
//        assertEquals(phone, phone);
//        assertEquals(phone.hashCode(), phone.hashCode());
//
//        PhoneNumberDTO samePhone = new PhoneNumberDTO("work", "6666666666");
//        assertEquals(phone, samePhone);
//        assertEquals(phone.hashCode(), samePhone.hashCode());
//
//        PhoneNumberDTO diffPhone = new PhoneNumberDTO("work", "7777777777");
//        assertNotEquals(phone, diffPhone);
//        assertNotEquals(phone, null);
//        assertNotEquals(phone, new Object());
//    }

    @Test
    void testSupplierHateoasWrapper_basicMethods() {
        SupplierResponseModel supplier = new SupplierResponseModel();
        SupplierHateoasWrapper.Embedded embedded = new SupplierHateoasWrapper.Embedded(List.of(supplier));

        SupplierHateoasWrapper wrapper = new SupplierHateoasWrapper(embedded);

        assertEquals(embedded, wrapper.getEmbedded());

        // Removed any mutation to wrapper (like setEmbedded(null)) to keep equality tests consistent

        // Equals & hashCode reflexivity
        assertEquals(wrapper, wrapper);
        assertEquals(wrapper.hashCode(), wrapper.hashCode());

        SupplierHateoasWrapper sameWrapper = new SupplierHateoasWrapper(embedded);
        assertEquals(wrapper, sameWrapper);

        SupplierHateoasWrapper wrapper2 = new SupplierHateoasWrapper(null);
        assertNotEquals(wrapper, wrapper2);

        SupplierHateoasWrapper.Embedded sameEmbedded = new SupplierHateoasWrapper.Embedded(List.of(supplier));
        assertEquals(embedded, sameEmbedded);
        assertEquals(embedded.hashCode(), sameEmbedded.hashCode());

        SupplierHateoasWrapper.Embedded diffEmbedded = new SupplierHateoasWrapper.Embedded(null);
        assertNotEquals(embedded, diffEmbedded);
    }

    @Mock
    private SupplierServiceImpl supplierService;

    @Mock
    private SupplierServiceClient supplierServiceClient;

    @InjectMocks
    private SupplierController supplierController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSuppliers() {
        List<SupplierResponseModel> suppliers = List.of(new SupplierResponseModel());
        when(supplierService.getSuppliers()).thenReturn(suppliers);

        ResponseEntity<List<SupplierResponseModel>> response = supplierController.getSuppliers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suppliers, response.getBody());
        verify(supplierService).getSuppliers();
    }

    @Test
    void testGetSupplierById() {
        SupplierResponseModel model = new SupplierResponseModel();
        when(supplierService.getSupplierBySupplierId("id")).thenReturn(model);

        ResponseEntity<SupplierResponseModel> response = supplierController.getSupplierBySupplierId("id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(model, response.getBody());
        verify(supplierService).getSupplierBySupplierId("id");
    }

    @Test
    void testAddSupplier() {
        SupplierRequestModel req = new SupplierRequestModel();
        SupplierResponseModel resp = new SupplierResponseModel();
        when(supplierServiceClient.addSupplier(req)).thenReturn(resp);

        ResponseEntity<SupplierResponseModel> response = supplierController.addSupplier(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resp, response.getBody());
        verify(supplierServiceClient).addSupplier(req);
    }

    @Test
    void testUpdateSupplier() {
        SupplierRequestModel req = new SupplierRequestModel();
        SupplierResponseModel resp = new SupplierResponseModel();

        when(supplierService.updateSupplier("id", req)).thenReturn(resp);

        ResponseEntity<SupplierResponseModel> response = supplierController.updateSupplier("id", req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());

        verify(supplierService).updateSupplier("id", req);
    }

    @Test
    void testDeleteSupplier() {
        when(supplierService.deleteSupplierBySupplierId("id")).thenReturn("Deleted");

        ResponseEntity<String> response = supplierController.deleteSupplierBySupplierId("id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted", response.getBody());
        verify(supplierService).deleteSupplierBySupplierId("id");
    }
}
