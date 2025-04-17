package com.champsoft.services.suppliers.Mapper;

import com.champsoft.services.suppliers.DataLayer.Supplier;
import com.champsoft.services.suppliers.Mapperlayer.SupplierRequestMapper;
import com.champsoft.services.suppliers.Mapperlayer.SupplierResponseMapper;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.champsoft.services.suppliers.PresentationLayer.SupplierResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierMapperTest {

    @Mock
    private SupplierRequestMapper requestMapper;

    @Mock
    private SupplierResponseMapper responseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestModelToEntity() {
        SupplierRequestModel requestModel = new SupplierRequestModel();
        requestModel.setSupplierId("sup-001");
        requestModel.setPassword1("password");

        Supplier supplier = new Supplier();
        supplier.setSupplierIdentifier("sup-001");
        supplier.setPassword("password");

        when(requestMapper.requestModelToEntity(requestModel)).thenReturn(supplier);

        Supplier result = requestMapper.requestModelToEntity(requestModel);

        assertNotNull(result);
        assertEquals("sup-001", result.getSupplierIdentifier());
        assertEquals("password", result.getPassword());
    }

    @Test
    void testEntityToResponseModel() {
        Supplier supplier = new Supplier();
        supplier.setSupplierIdentifier("sup-001");

        SupplierResponseModel responseModel = new SupplierResponseModel();
        responseModel.setSupplierId("sup-001");

        when(responseMapper.entityToResponseModel(supplier)).thenReturn(responseModel);

        SupplierResponseModel result = responseMapper.entityToResponseModel(supplier);

        assertNotNull(result);
        assertEquals("sup-001", result.getSupplierId());
    }
}