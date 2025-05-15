package org.example.Sales.PresentationLayer.inventorydtos;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlowerResponseModelTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        FlowerResponseModel flower = new FlowerResponseModel();
        flower.setFlowerId("f123");
        flower.setInventoryId("inv456");
        flower.setFlowerName("Rose");
        flower.setFlowerColor("Red");
        flower.setFlowerCategory("Garden");
        flower.setFlowerStatus("Available");
        flower.setStockQuantity(100);
        flower.setSupplierIdentifier("sup789");
        flower.setPrice(new BigDecimal("12.50"));
        flower.setCurrency("USD");

        List<OptionDto> options = new ArrayList<>();
        options.add(new OptionDto("Gift Wrap", "Special wrapping", new BigDecimal("2.00")));
        flower.setOptions(options);

        assertEquals("f123", flower.getFlowerId());
        assertEquals("inv456", flower.getInventoryId());
        assertEquals("Rose", flower.getFlowerName());
        assertEquals("Red", flower.getFlowerColor());
        assertEquals("Garden", flower.getFlowerCategory());
        assertEquals("Available", flower.getFlowerStatus());
        assertEquals(100, flower.getStockQuantity());
        assertEquals("sup789", flower.getSupplierIdentifier());
        assertEquals(new BigDecimal("12.50"), flower.getPrice());
        assertEquals("USD", flower.getCurrency());
        assertEquals(1, flower.getOptions().size());
        assertEquals("Gift Wrap", flower.getOptions().get(0).getName());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        List<OptionDto> options = List.of(new OptionDto("Gift Wrap", "Special wrapping", new BigDecimal("2.00")));
        FlowerResponseModel flower = new FlowerResponseModel(
                "f123", "inv456", "Rose", "Red", "Garden", "Available",
                100, "sup789", new BigDecimal("12.50"), "USD", options
        );

        assertEquals("f123", flower.getFlowerId());
        assertEquals("inv456", flower.getInventoryId());
        assertEquals("Rose", flower.getFlowerName());
        assertEquals("Red", flower.getFlowerColor());
        assertEquals("Garden", flower.getFlowerCategory());
        assertEquals("Available", flower.getFlowerStatus());
        assertEquals(100, flower.getStockQuantity());
        assertEquals("sup789", flower.getSupplierIdentifier());
        assertEquals(new BigDecimal("12.50"), flower.getPrice());
        assertEquals("USD", flower.getCurrency());
        assertEquals(1, flower.getOptions().size());
        assertEquals("Gift Wrap", flower.getOptions().get(0).getName());
    }
}

class InventoryResponseModelTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        InventoryResponseModel inventory = new InventoryResponseModel();
        inventory.setInventoryId("inv123");
        inventory.setType("Flower");

        assertEquals("inv123", inventory.getInventoryId());
        assertEquals("Flower", inventory.getType());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        InventoryResponseModel inventory = new InventoryResponseModel("inv123", "Flower");

        assertEquals("inv123", inventory.getInventoryId());
        assertEquals("Flower", inventory.getType());
    }
}

class OptionDtoTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        OptionDto option = new OptionDto();
        option.setName("Gift Wrap");
        option.setDescription("Special wrapping");
        option.setPrice(new BigDecimal("2.00"));

        assertEquals("Gift Wrap", option.getName());
        assertEquals("Special wrapping", option.getDescription());
        assertEquals(new BigDecimal("2.00"), option.getPrice());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        OptionDto option = new OptionDto("Gift Wrap", "Special wrapping", new BigDecimal("2.00"));

        assertEquals("Gift Wrap", option.getName());
        assertEquals("Special wrapping", option.getDescription());
        assertEquals(new BigDecimal("2.00"), option.getPrice());
    }

    @Test
    void testEquals_reflexive() {
        FlowerResponseModel flower = createSampleFlower();
        assertEquals(flower, flower);
    }

    @Test
    void testEquals_sameValues_true() {
        FlowerResponseModel flower1 = createSampleFlower();
        FlowerResponseModel flower2 = createSampleFlower();

        assertEquals(flower1, flower2);
        assertEquals(flower2, flower1);
    }

    @Test
    void testEquals_differentValues_false() {
        FlowerResponseModel flower1 = createSampleFlower();
        FlowerResponseModel flower2 = createSampleFlower();
        flower2.setFlowerName("DifferentName");

        assertNotEquals(flower1, flower2);
    }

    @Test
    void testEquals_null_false() {
        FlowerResponseModel flower = createSampleFlower();
        assertNotEquals(null, flower);
    }

    @Test
    void testEquals_differentClass_false() {
        FlowerResponseModel flower = createSampleFlower();
        Object other = new Object();
        assertNotEquals(flower, other);
    }

    private FlowerResponseModel createSampleFlower() {
        List<OptionDto> options = List.of(new OptionDto("Gift Wrap", "Special wrapping", new BigDecimal("2.00")));
        return new FlowerResponseModel(
                "f123", "inv456", "Rose", "Red", "Garden", "Available",
                100, "sup789", new BigDecimal("12.50"), "USD", options
        );
    }
}

class InventoryResponseModelEqualsTest {

    @Test
    void testEquals_reflexive() {
        InventoryResponseModel inventory = new InventoryResponseModel("id123", "typeA");
        assertEquals(inventory, inventory);
    }

    @Test
    void testEquals_sameValues_true() {
        InventoryResponseModel inv1 = new InventoryResponseModel("id123", "typeA");
        InventoryResponseModel inv2 = new InventoryResponseModel("id123", "typeA");

        assertEquals(inv1, inv2);
        assertEquals(inv2, inv1);
    }

    @Test
    void testEquals_differentValues_false() {
        InventoryResponseModel inv1 = new InventoryResponseModel("id123", "typeA");
        InventoryResponseModel inv2 = new InventoryResponseModel("id999", "typeA");

        assertNotEquals(inv1, inv2);
    }

    @Test
    void testEquals_null_false() {
        InventoryResponseModel inventory = new InventoryResponseModel("id123", "typeA");
        assertNotEquals(null, inventory);
    }

    @Test
    void testEquals_differentClass_false() {
        InventoryResponseModel inventory = new InventoryResponseModel("id123", "typeA");
        Object other = new Object();
        assertNotEquals(inventory, other);
    }
}

class OptionDtoEqualsTest {

    @Test
    void testEquals_reflexive() {
        OptionDto option = new OptionDto("Name", "Desc", new BigDecimal("1.00"));
        assertEquals(option, option);
    }

    @Test
    void testEquals_sameValues_true() {
        OptionDto option1 = new OptionDto("Name", "Desc", new BigDecimal("1.00"));
        OptionDto option2 = new OptionDto("Name", "Desc", new BigDecimal("1.00"));

        assertEquals(option1, option2);
        assertEquals(option2, option1);
    }

    @Test
    void testEquals_differentValues_false() {
        OptionDto option1 = new OptionDto("Name", "Desc", new BigDecimal("1.00"));
        OptionDto option2 = new OptionDto("Name", "OtherDesc", new BigDecimal("1.00"));

        assertNotEquals(option1, option2);
    }

    @Test
    void testEquals_null_false() {
        OptionDto option = new OptionDto("Name", "Desc", new BigDecimal("1.00"));
        assertNotEquals(null, option);
    }

    @Test
    void testEquals_differentClass_false() {
        OptionDto option = new OptionDto("Name", "Desc", new BigDecimal("1.00"));
        Object other = new Object();
        assertNotEquals(option, other);
    }
}
