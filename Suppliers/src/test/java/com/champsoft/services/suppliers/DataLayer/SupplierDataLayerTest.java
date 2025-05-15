package com.champsoft.services.suppliers.DataLayer;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierDataLayerTest {

    @Test
    void testSupplierConstructorAndGetters() {
        Address address = new Address("123 Main", "H1A 1A1", "Montreal", "Quebec");
        SupplierPhoneNumber phone = new SupplierPhoneNumber(PhoneType.MOBILE, "514-123-4567");
        Supplier supplier = new Supplier("id123", "sup-0001", "Company", "Person", "email@example.com", "username", "pass", address, List.of(phone));

        assertEquals("Company", supplier.getCompanyName());
        assertEquals("Montreal", supplier.getAddress().getCity());
        assertEquals("514-123-4567", supplier.getPhoneNumbers().get(0).getNumber());
    }

    @Test
    void testSupplierGenerateIdentifierIfMissing_setsUUID() {
        Supplier supplier = new Supplier();
        supplier.setSupplierIdentifier("");
        supplier.generateIdentifierIfMissing();

        assertNotNull(supplier.getSupplierIdentifier());
        assertFalse(supplier.getSupplierIdentifier().isEmpty());
    }

    @Test
    void testSupplierGenerateIdentifierIfAlreadySet() {
        Supplier supplier = new Supplier();
        supplier.setSupplierIdentifier("already-set");
        supplier.generateIdentifierIfMissing();

        assertEquals("already-set", supplier.getSupplierIdentifier());
    }

    @Test
    void testAddressConstructorAndGetters() {
        Address address = new Address("123 Street", "H1A 1A1", "Montreal", "Quebec");

        assertEquals("123 Street", address.getStreetAddress());
        assertEquals("Quebec", address.getProvince());
    }

    @Test
    void testAddressSettersAndEquals() {
        Address a1 = new Address();
        Address a2 = new Address();

        a1.setCity("Montreal");
        a2.setCity("Montreal");

        assertEquals(a1, a2);
    }

    @Test
    void testPhoneNumberConstructorAndGetters() {
        SupplierPhoneNumber phone = new SupplierPhoneNumber(PhoneType.WORK, "123-456-7890");

        assertEquals(PhoneType.WORK, phone.getType());
        assertEquals("123-456-7890", phone.getNumber());
    }

    @Test
    void testPhoneNumberSettersAndEquals() {
        SupplierPhoneNumber p1 = new SupplierPhoneNumber();
        SupplierPhoneNumber p2 = new SupplierPhoneNumber();

        p1.setNumber("514");
        p2.setNumber("514");

        assertEquals(p1, p2);
    }

    @Test
    void testPhoneTypeEnum() {
        assertEquals(PhoneType.MOBILE, PhoneType.valueOf("MOBILE"));
        assertEquals(4, PhoneType.values().length);
    }
}
