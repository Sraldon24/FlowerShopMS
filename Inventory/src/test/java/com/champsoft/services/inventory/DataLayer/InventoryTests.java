package com.champsoft.services.inventory.DataLayer;

import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.PresentationLayer.supplierdtos.PhoneType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTests {
    @Test
    public void testInventoryNoArgsConstructorAndSetters() {
        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setType("Main");
        inventory.setInventoryIdentifier(new InventoryIdentifier("INV-001"));

        assertEquals(1, inventory.getId());
        assertEquals("Main", inventory.getType());
        assertEquals("INV-001", inventory.getInventoryIdentifier().getInventoryId());
    }

    @Test
    public void testInventoryAllArgsConstructor() {
        InventoryIdentifier identifier = new InventoryIdentifier("INV-002");
        Inventory inventory = new Inventory(2, identifier, "Backup");

        assertEquals(2, inventory.getId());
        assertEquals("Backup", inventory.getType());
        assertEquals("INV-002", inventory.getInventoryIdentifier().getInventoryId());
    }

    @Test
    public void testPrePersist_GeneratesIdentifierIfMissing() {
        Inventory inventory = new Inventory();
        inventory.setType("Auto");

        inventory.prePersist();

        assertNotNull(inventory.getInventoryIdentifier());
        String generatedId = inventory.getInventoryIdentifier().getInventoryId();
        assertNotNull(generatedId);
        assertFalse(generatedId.isEmpty());
        assertEquals(36, generatedId.length());
    }

    @Test
    public void testInventoryIdentifierNoArgsConstructorAndSetters() {
        InventoryIdentifier identifier = new InventoryIdentifier();
        identifier.setInventoryId("INV-123");

        assertEquals("INV-123", identifier.getInventoryId());
    }

    @Test
    public void testInventoryIdentifierAllArgsConstructor() {
        InventoryIdentifier identifier = new InventoryIdentifier("INV-456");

        assertEquals("INV-456", identifier.getInventoryId());
    }

    @Test
    public void testGenerateIfMissing_GeneratesUUIDIfNull() {
        InventoryIdentifier identifier = new InventoryIdentifier(null);
        identifier.generateIfMissing();

        assertNotNull(identifier.getInventoryId());
        assertFalse(identifier.getInventoryId().isEmpty());
        assertEquals(36, identifier.getInventoryId().length());
    }

    @Test
    public void testGenerateIfMissing_DoesNotOverrideExisting() {
        InventoryIdentifier identifier = new InventoryIdentifier("EXISTING-ID");
        identifier.generateIfMissing();

        assertEquals("EXISTING-ID", identifier.getInventoryId());
    }


    @Test
    public void testEnumConstantsExist() {
        // Test if all expected enum constants exist
        assertEquals(4, PhoneType.values().length, "PhoneType should have exactly 4 constants");
        assertNotNull(PhoneType.valueOf("MOBILE"), "MOBILE should be a valid PhoneType");
        assertNotNull(PhoneType.valueOf("HOME"), "HOME should be a valid PhoneType");
        assertNotNull(PhoneType.valueOf("OFFICE"), "OFFICE should be a valid PhoneType");
        assertNotNull(PhoneType.valueOf("WORK"), "WORK should be a valid PhoneType");
    }

    @Test
    public void testEnumOrder() {
        // Check the order of the enum constants
        PhoneType[] expectedOrder = {PhoneType.MOBILE, PhoneType.HOME, PhoneType.OFFICE, PhoneType.WORK};
        assertArrayEquals(expectedOrder, PhoneType.values(), "Enum order should be MOBILE, HOME, OFFICE, WORK");
    }
}
