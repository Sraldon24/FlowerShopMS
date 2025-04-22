package com.champsoft.services.inventory.DataLayer;

import com.champsoft.services.inventory.DataLayer.Flowers.*;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.utils.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FlowerTests {

    @Test
    public void testAllArgsConstructorAndGetters() {
        FlowersIdentifier flowersIdentifier = new FlowersIdentifier("F123");
        InventoryIdentifier inventoryIdentifier = new InventoryIdentifier("INV456");
        Price price = new Price(new BigDecimal("10.99"), Currency.USD);
        Option option1 = new Option("Gift Wrap", "Nice wrapping", new BigDecimal("2.00"));
        Option option2 = new Option("Vase", "Glass vase", new BigDecimal("5.00"));

        Flower flower = new Flower(
                1,
                flowersIdentifier,
                inventoryIdentifier,
                "Rose",
                "Red",
                "Valentine",
                Status.AVAILABLE,
                100,
                "SUP789",
                List.of(option1, option2),
                price
        );

        assertEquals(1, flower.getId());
        assertEquals(flowersIdentifier, flower.getFlowersIdentifier());
        assertEquals(inventoryIdentifier, flower.getInventoryIdentifier());
        assertEquals("Rose", flower.getFlowerName());
        assertEquals("Red", flower.getFlowerColor());
        assertEquals("Valentine", flower.getFlowerCategory());
        assertEquals(Status.AVAILABLE, flower.getStatus());
        assertEquals(100, flower.getStockQuantity());
        assertEquals("SUP789", flower.getSupplierIdentifier());
        assertEquals(2, flower.getOptions().size());
        assertEquals(price, flower.getPrice());
    }

    @Test
    public void testNoArgsConstructorAndSetters() {
        Flower flower = new Flower();

        flower.setId(2);
        FlowersIdentifier flowersIdentifier = new FlowersIdentifier("F456");
        flower.setFlowersIdentifier(flowersIdentifier);
        InventoryIdentifier inventoryIdentifier = new InventoryIdentifier("INV789");
        flower.setInventoryIdentifier(inventoryIdentifier);
        flower.setFlowerName("Tulip");
        flower.setFlowerColor("Yellow");
        flower.setFlowerCategory("Spring");
        flower.setStatus(Status.OUT_OF_STOCK);
        flower.setStockQuantity(0);
        flower.setSupplierIdentifier("SUP123");

        Option option = new Option("Note", "Personal message", new BigDecimal("1.00"));
        flower.setOptions(List.of(option));

        Price price = new Price(new BigDecimal("5.99"), Currency.EUR);
        flower.setPrice(price);

        assertEquals("Tulip", flower.getFlowerName());
        assertEquals(Status.OUT_OF_STOCK, flower.getStatus());
        assertEquals("SUP123", flower.getSupplierIdentifier());
        assertEquals("Yellow", flower.getFlowerColor());
        assertEquals("Spring", flower.getFlowerCategory());
        assertEquals(0, flower.getStockQuantity());
        assertEquals("INV789", flower.getInventoryIdentifier().getInventoryId());
        assertEquals("F456", flower.getFlowersIdentifier().getFlowerNumber());
        assertEquals(Currency.EUR, flower.getPrice().getCurrency());
    }

    @Test
    public void testPrePersistWithNullFlowersIdentifier() {
        Flower flower = new Flower();
        flower.setFlowersIdentifier(null);

        flower.ensureFlowerNumberExists();

        assertNotNull(flower.getFlowersIdentifier());
        assertNotNull(flower.getFlowersIdentifier().getFlowerNumber());
    }

    @Test
    public void testPrePersistWithExistingFlowersIdentifier() {
        FlowersIdentifier flowersIdentifier = new FlowersIdentifier("FLOWER999");
        Flower flower = new Flower();
        flower.setFlowersIdentifier(flowersIdentifier);

        flower.ensureFlowerNumberExists();

        assertEquals("FLOWER999", flower.getFlowersIdentifier().getFlowerNumber());
    }

    @Test
    public void testGenerateIfMissing_GeneratesNewIfNull() {
        FlowersIdentifier identifier = new FlowersIdentifier(null);
        identifier.generateIfMissing();

        assertNotNull(identifier.getFlowerNumber());
        assertTrue(identifier.getFlowerNumber().startsWith("flw-"));
        assertEquals(17, identifier.getFlowerNumber().length());
    }

    @Test
    public void testGenerateIfMissing_DoesNotChangeExisting() {
        FlowersIdentifier identifier = new FlowersIdentifier("flw-123456789abcd");
        identifier.generateIfMissing();

        assertEquals("flw-123456789abcd", identifier.getFlowerNumber());
    }

    @Test
    public void testGenerateIfMissing_CreatesNewIfNull() {
        FlowersIdentifier identifier = new FlowersIdentifier(null);
        identifier.generateIfMissing();

        assertNotNull(identifier.getFlowerNumber());
        assertTrue(identifier.getFlowerNumber().startsWith("flw-"));
        assertEquals(17, identifier.getFlowerNumber().length()); // "flw-" + 13 chars
    }

    @Test
    public void testGenerateIfMissing_DoesNotOverrideExisting() {
        FlowersIdentifier identifier = new FlowersIdentifier("flw-fixed-id");
        identifier.generateIfMissing();

        assertEquals("flw-fixed-id", identifier.getFlowerNumber());
    }
    @Autowired
    private FlowerRepository flowerRepository;

    @Test
    public void testFindByFlowersIdentifier_FlowerNumber() {
        Flower flower = new Flower();
        flower.setFlowersIdentifier(new FlowersIdentifier("flw-find-1"));
        flower.setInventoryIdentifier(new InventoryIdentifier("INV-001"));
        flower.setFlowerName("Rose");
        flower.setSupplierIdentifier("SUP-1");
        flower.setStatus(Status.AVAILABLE);
        flower.setStockQuantity(10);
        flowerRepository.save(flower);

        var result = flowerRepository.findByFlowersIdentifier_FlowerNumber("flw-find-1");
        assertTrue(result.isPresent());
        assertEquals("flw-find-1", result.get().getFlowersIdentifier().getFlowerNumber());
    }

    @Test
    public void testFindByInventoryIdAndFlowerNumber() {
        Flower flower = new Flower();
        flower.setFlowersIdentifier(new FlowersIdentifier("flw-combo-1"));
        flower.setInventoryIdentifier(new InventoryIdentifier("INV-123"));
        flower.setFlowerName("Tulip");
        flower.setSupplierIdentifier("SUP-2");
        flower.setStatus(Status.AVAILABLE);
        flower.setStockQuantity(20);
        flowerRepository.save(flower);

        var result = flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber("INV-123", "flw-combo-1");
        assertTrue(result.isPresent());
        assertEquals("flw-combo-1", result.get().getFlowersIdentifier().getFlowerNumber());
    }

    @Test
    public void testFindAllByInventoryIdentifier_InventoryId() {
        Flower flower1 = new Flower();
        flower1.setFlowersIdentifier(new FlowersIdentifier("flw-1"));
        flower1.setInventoryIdentifier(new InventoryIdentifier("INV-555"));
        flower1.setFlowerName("Lily");
        flower1.setSupplierIdentifier("SUP-3");
        flower1.setStatus(Status.AVAILABLE);
        flower1.setStockQuantity(5);
        flowerRepository.save(flower1);

        Flower flower2 = new Flower();
        flower2.setFlowersIdentifier(new FlowersIdentifier("flw-2"));
        flower2.setInventoryIdentifier(new InventoryIdentifier("INV-555"));
        flower2.setFlowerName("Daisy");
        flower2.setSupplierIdentifier("SUP-4");
        flower2.setStatus(Status.AVAILABLE);
        flower2.setStockQuantity(8);
        flowerRepository.save(flower2);

        List<Flower> results = flowerRepository.findAllByInventoryIdentifier_InventoryId("INV-555");
        assertEquals(2, results.size());
    }

    @Test
    public void testUsageTypeValues() {
        UsageType[] values = UsageType.values();
        assertEquals(2, values.length);
        assertTrue(List.of(values).contains(UsageType.NEW));
        assertTrue(List.of(values).contains(UsageType.USED));
    }

    @Test
    public void testUsageTypeValueOf() {
        assertEquals(UsageType.NEW, UsageType.valueOf("NEW"));
        assertEquals(UsageType.USED, UsageType.valueOf("USED"));
    }
}


