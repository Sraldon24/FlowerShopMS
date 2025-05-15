package org.example;

import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.example.Inventory.PresentationLayer.Flower.FlowerRequestModel;
import org.example.Inventory.PresentationLayer.Flower.OptionDto;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlowerModelTest {

    @Test
    void testFlowerResponseModelConstructorsAndGettersSetters() {
        SupplierResponseModel supplier = new SupplierResponseModel();

        List<OptionDto> options = Arrays.asList(
                new OptionDto("Opt1", "Description1", new BigDecimal("10.00")),
                new OptionDto("Opt2", "Description2", new BigDecimal("20.00"))
        );

        FlowerResponseModel model = new FlowerResponseModel(
                "fid", "iid", "Rose", "Red", "Category1", "Available",
                100, "supplierId", new BigDecimal("12.34"), "USD",
                options, supplier);

        assertEquals("fid", model.getFlowerId());
        assertEquals("iid", model.getInventoryId());
        assertEquals("Rose", model.getFlowerName());
        assertEquals("Red", model.getFlowerColor());
        assertEquals("Category1", model.getFlowerCategory());
        assertEquals("Available", model.getFlowerStatus());
        assertEquals(100, model.getStockQuantity());
        assertEquals("supplierId", model.getSupplierIdentifier());
        assertEquals(new BigDecimal("12.34"), model.getPrice());
        assertEquals("USD", model.getCurrency());
        assertEquals(options, model.getOptions());
        assertEquals(supplier, model.getSupplier());

        // Setters test
        model.setFlowerId("fid2");
        model.setInventoryId("iid2");
        model.setFlowerName("Tulip");
        model.setFlowerColor("Yellow");
        model.setFlowerCategory("Category2");
        model.setFlowerStatus("OutOfStock");
        model.setStockQuantity(50);
        model.setSupplierIdentifier("supplierId2");
        model.setPrice(new BigDecimal("45.67"));
        model.setCurrency("EUR");

        List<OptionDto> newOptions = Arrays.asList(
                new OptionDto("Opt3", "Description3", new BigDecimal("30.00"))
        );
        model.setOptions(newOptions);

        SupplierResponseModel supplier2 = new SupplierResponseModel();
        model.setSupplier(supplier2);

        assertEquals("fid2", model.getFlowerId());
        assertEquals("iid2", model.getInventoryId());
        assertEquals("Tulip", model.getFlowerName());
        assertEquals("Yellow", model.getFlowerColor());
        assertEquals("Category2", model.getFlowerCategory());
        assertEquals("OutOfStock", model.getFlowerStatus());
        assertEquals(50, model.getStockQuantity());
        assertEquals("supplierId2", model.getSupplierIdentifier());
        assertEquals(new BigDecimal("45.67"), model.getPrice());
        assertEquals("EUR", model.getCurrency());
        assertEquals(newOptions, model.getOptions());
        assertEquals(supplier2, model.getSupplier());

        String toString = model.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("fid2"));

        FlowerResponseModel modelCopy = new FlowerResponseModel();
        modelCopy.setFlowerId("fid2");
        modelCopy.setInventoryId("iid2");
        modelCopy.setFlowerName("Tulip");
        modelCopy.setFlowerColor("Yellow");
        modelCopy.setFlowerCategory("Category2");
        modelCopy.setFlowerStatus("OutOfStock");
        modelCopy.setStockQuantity(50);
        modelCopy.setSupplierIdentifier("supplierId2");
        modelCopy.setPrice(new BigDecimal("45.67"));
        modelCopy.setCurrency("EUR");
        modelCopy.setOptions(newOptions);
        modelCopy.setSupplier(supplier2);

        assertEquals(model, modelCopy);
        assertEquals(model.hashCode(), modelCopy.hashCode());
    }

    @Test
    void testFlowerRequestModelConstructorsAndGettersSetters() {
        List<OptionDto> options = Arrays.asList(
                new OptionDto("Opt1", "Description1", new BigDecimal("10.00")),
                new OptionDto("Opt2", "Description2", new BigDecimal("20.00"))
        );

        FlowerRequestModel model = new FlowerRequestModel(
                "fid", "iid", "Rose", "Red", "Category1", "Available",
                100, "supplierId", new BigDecimal("12.34"), "USD",
                options);

        assertEquals("fid", model.getFlowerId());
        assertEquals("iid", model.getInventoryId());
        assertEquals("Rose", model.getFlowerName());
        assertEquals("Red", model.getFlowerColor());
        assertEquals("Category1", model.getFlowerCategory());
        assertEquals("Available", model.getFlowerStatus());
        assertEquals(100, model.getStockQuantity());
        assertEquals("supplierId", model.getSupplierIdentifier());
        assertEquals(new BigDecimal("12.34"), model.getPrice());
        assertEquals("USD", model.getCurrency());
        assertEquals(options, model.getOptions());

        // Setters test
        model.setFlowerId("fid2");
        model.setInventoryId("iid2");
        model.setFlowerName("Tulip");
        model.setFlowerColor("Yellow");
        model.setFlowerCategory("Category2");
        model.setFlowerStatus("OutOfStock");
        model.setStockQuantity(50);
        model.setSupplierIdentifier("supplierId2");
        model.setPrice(new BigDecimal("45.67"));
        model.setCurrency("EUR");

        List<OptionDto> newOptions = Arrays.asList(
                new OptionDto("Opt3", "Description3", new BigDecimal("30.00"))
        );
        model.setOptions(newOptions);

        assertEquals("fid2", model.getFlowerId());
        assertEquals("iid2", model.getInventoryId());
        assertEquals("Tulip", model.getFlowerName());
        assertEquals("Yellow", model.getFlowerColor());
        assertEquals("Category2", model.getFlowerCategory());
        assertEquals("OutOfStock", model.getFlowerStatus());
        assertEquals(50, model.getStockQuantity());
        assertEquals("supplierId2", model.getSupplierIdentifier());
        assertEquals(new BigDecimal("45.67"), model.getPrice());
        assertEquals("EUR", model.getCurrency());
        assertEquals(newOptions, model.getOptions());

        String toString = model.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("fid2"));

        FlowerRequestModel modelCopy = new FlowerRequestModel();
        modelCopy.setFlowerId("fid2");
        modelCopy.setInventoryId("iid2");
        modelCopy.setFlowerName("Tulip");
        modelCopy.setFlowerColor("Yellow");
        modelCopy.setFlowerCategory("Category2");
        modelCopy.setFlowerStatus("OutOfStock");
        modelCopy.setStockQuantity(50);
        modelCopy.setSupplierIdentifier("supplierId2");
        modelCopy.setPrice(new BigDecimal("45.67"));
        modelCopy.setCurrency("EUR");
        modelCopy.setOptions(newOptions);

        assertEquals(model, modelCopy);
        assertEquals(model.hashCode(), modelCopy.hashCode());
    }
}
