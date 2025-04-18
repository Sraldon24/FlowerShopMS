package com.champsoft.services.suppliers.BusinessLayer;

import com.champsoft.services.suppliers.DataLayer.Supplier;
import com.champsoft.services.suppliers.DataLayer.SupplierRepository;
import com.champsoft.services.suppliers.Mapperlayer.SupplierRequestMapper;
import com.champsoft.services.suppliers.Mapperlayer.SupplierResponseMapper;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.champsoft.services.suppliers.PresentationLayer.SupplierResponseModel;
import com.champsoft.services.suppliers.utils.NotFoundException;
import com.champsoft.services.suppliers.utils.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierResponseMapper supplierResponseMapper;

    @Mock
    private SupplierRequestMapper supplierRequestMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplierEntity;
    private SupplierRequestModel requestModel;
    private SupplierResponseModel responseModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        supplierEntity = new Supplier();
        supplierEntity.setSupplierIdentifier("sup-001");

        requestModel = new SupplierRequestModel();
        requestModel.setSupplierId("sup-001");
        requestModel.setPassword1("pass");
        requestModel.setPassword2("pass");

        responseModel = new SupplierResponseModel();
        responseModel.setSupplierId("sup-001");
    }

    @Test
    void testGetSuppliers() {
        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplierEntity));
        when(supplierResponseMapper.entityListToResponseModelList(any())).thenReturn(Arrays.asList(responseModel));

        List<SupplierResponseModel> result = supplierService.getSuppliers();
        assertEquals(1, result.size());
        assertEquals("sup-001", result.get(0).getSupplierId());
    }

    @Test
    void testGetSupplierBySupplierId_found() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(supplierEntity);
        when(supplierResponseMapper.entityToResponseModel(any())).thenReturn(responseModel);

        SupplierResponseModel result = supplierService.getSupplierBySupplierId("sup-001");
        assertEquals("sup-001", result.getSupplierId());
    }

    @Test
    void testGetSupplierBySupplierId_notFound() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> supplierService.getSupplierBySupplierId("sup-001"));
    }

    @Test
    void testAddSupplier_valid() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(null);
        when(supplierRequestMapper.requestModelToEntity(any())).thenReturn(supplierEntity);
        when(supplierRepository.save(any())).thenReturn(supplierEntity);
        when(supplierResponseMapper.entityToResponseModel(any())).thenReturn(responseModel);

        SupplierResponseModel result = supplierService.addSupplier(requestModel);
        assertEquals("sup-001", result.getSupplierId());
    }

    @Test
    void testAddSupplier_passwordMismatch() {
        requestModel.setPassword2("wrongpass");
        assertThrows(InvalidInputException.class, () -> supplierService.addSupplier(requestModel));
    }

    @Test
    void testAddSupplier_duplicateSupplier() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(supplierEntity);
        assertThrows(InvalidInputException.class, () -> supplierService.addSupplier(requestModel));
    }

    @Test
    void testUpdateSupplier_valid() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(supplierEntity);
        when(supplierRequestMapper.requestModelToEntity(any())).thenReturn(supplierEntity);
        when(supplierRepository.save(any())).thenReturn(supplierEntity);
        when(supplierResponseMapper.entityToResponseModel(any())).thenReturn(responseModel);

        SupplierResponseModel result = supplierService.updateSupplier("sup-001", requestModel);
        assertEquals("sup-001", result.getSupplierId());
    }

    @Test
    void testUpdateSupplier_notFound() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> supplierService.updateSupplier("sup-001", requestModel));
    }

    @Test
    void testUpdateSupplier_passwordMismatch() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(supplierEntity);
        requestModel.setPassword2("wrongpass");
        assertThrows(InvalidInputException.class, () -> supplierService.updateSupplier("sup-001", requestModel));
    }

    @Test
    void testDeleteSupplier_valid() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(supplierEntity);
        String result = supplierService.deleteSupplierBySupplierId("sup-001");
        assertTrue(result.contains("deleted"));
    }

    @Test
    void testDeleteSupplier_notFound() {
        when(supplierRepository.findSupplierBySupplierIdentifier("sup-001")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> supplierService.deleteSupplierBySupplierId("sup-001"));
    }
}
