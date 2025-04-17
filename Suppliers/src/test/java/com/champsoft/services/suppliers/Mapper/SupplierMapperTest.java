package com.champsoft.services.suppliers.Mapper;

import com.champsoft.services.suppliers.DataLayer.Address;
import com.champsoft.services.suppliers.DataLayer.PhoneType;
import com.champsoft.services.suppliers.DataLayer.Supplier;
import com.champsoft.services.suppliers.DataLayer.SupplierPhoneNumber;
import com.champsoft.services.suppliers.Mapperlayer.SupplierRequestMapperImpl;
import com.champsoft.services.suppliers.Mapperlayer.SupplierResponseMapperImpl;
import com.champsoft.services.suppliers.PresentationLayer.PhoneNumberDTO;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.champsoft.services.suppliers.PresentationLayer.SupplierResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierMapperTest {

    private SupplierRequestMapperImpl requestMapper;
    private SupplierResponseMapperImpl responseMapper;

    @BeforeEach
    void setUp() {
        requestMapper = new SupplierRequestMapperImpl();
        responseMapper = new SupplierResponseMapperImpl();
    }

    @Test
    void testRequestModelToEntity() {
        SupplierRequestModel request = new SupplierRequestModel(
                "sup-1010", "TestCo", "John Doe", "john@test.com", "123 Main St",
                "H2X 3T1", "Montreal", "Quebec", "johnd", "pw", "pw",
                List.of(new SupplierPhoneNumber(PhoneType.MOBILE, "514-111-2222"))
        );

        Supplier supplier = requestMapper.requestModelToEntity(request);

        assertEquals("TestCo", supplier.getCompanyName());
        assertEquals("Montreal", supplier.getAddress().getCity());        // ✅ Fixed
        assertEquals("Quebec", supplier.getAddress().getProvince());     // ✅ Fixed
        assertEquals(1, supplier.getPhoneNumbers().size());
    }

    @Test
    void testEntityToResponseModel() {
        Supplier supplier = new Supplier();
        supplier.setSupplierIdentifier("sup-1010");
        supplier.setCompanyName("TestCo");
        supplier.setContactPerson("John Doe");
        supplier.setEmailAddress("john@test.com");
        supplier.setAddress(new Address("123 Main St", "Montreal", "Quebec", "H2X 3T1"));
        supplier.setPhoneNumbers(List.of(new SupplierPhoneNumber(PhoneType.MOBILE, "514-111-2222")));

        SupplierResponseModel response = responseMapper.entityToResponseModel(supplier);

        assertEquals("TestCo", response.getCompanyName());
        assertEquals("Montreal", response.getCity());           // ✅ Fixed assertion
        assertEquals("Quebec", response.getProvince());         // ✅ New assertion
        assertEquals(1, response.getPhoneNumbers().size());
        assertEquals("MOBILE", response.getPhoneNumbers().get(0).getType());
        assertEquals("514-111-2222", response.getPhoneNumbers().get(0).getNumber());
    }

    @Test
    void testPhoneNumberToDto() {
        SupplierPhoneNumber phone = new SupplierPhoneNumber(PhoneType.WORK, "123-456-7890");

        PhoneNumberDTO dto = responseMapper.phoneNumberToDto(phone);

        assertEquals("WORK", dto.getType());
        assertEquals("123-456-7890", dto.getNumber());
    }
}
