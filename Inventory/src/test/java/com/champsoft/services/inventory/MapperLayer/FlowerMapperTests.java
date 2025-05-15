package com.champsoft.services.inventory.MapperLayer;

import com.champsoft.services.inventory.DataLayer.Flowers.*;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerRequestMapperImpl;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import com.champsoft.services.inventory.PresentationLayer.OptionDto;
import com.champsoft.services.inventory.utils.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan("com.champsoft.services.inventory.MapperLayer")
public class FlowerMapperTests {
    @Autowired
    private FlowerRequestMapper flowerRequestMapper;

    @Autowired
    private FlowerResponseMapper flowerResponseMapper;
    @Test
    public void testRequestModelToEntity() {

        FlowerRequestModel flowerRequestModel = new FlowerRequestModel();
        flowerRequestModel.setFlowerId("FLOWER123");
        flowerRequestModel.setInventoryId("INV123");
        flowerRequestModel.setFlowerName("Rose");
        flowerRequestModel.setFlowerColor("Red");
        flowerRequestModel.setFlowerCategory("Category1");
        flowerRequestModel.setFlowerStatus("AVAILABLE");
        flowerRequestModel.setStockQuantity(100);
        flowerRequestModel.setSupplierIdentifier("SUP123");

        List<OptionDto> options = new ArrayList<>();
        options.add(new OptionDto("Vase", "Glass Vase", BigDecimal.valueOf(5.00)));
        flowerRequestModel.setOptions(options);

        flowerRequestModel.setPrice(BigDecimal.valueOf(10.00));
        flowerRequestModel.setCurrency("USD");

        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);

        assertNotNull(flower);
        assertEquals("FLOWER123", flower.getFlowersIdentifier().getFlowerNumber());
        assertEquals("INV123", flower.getInventoryIdentifier().getInventoryId());
        assertEquals("Rose", flower.getFlowerName());
        assertEquals("Red", flower.getFlowerColor());
        assertEquals("Category1", flower.getFlowerCategory());
        assertEquals(Status.AVAILABLE, flower.getStatus());
        assertEquals(100, flower.getStockQuantity());
        assertEquals("SUP123", flower.getSupplierIdentifier());

        assertNotNull(flower.getOptions());
        assertEquals(1, flower.getOptions().size());
        assertEquals("Vase", flower.getOptions().get(0).getName());
        assertEquals("Glass Vase", flower.getOptions().get(0).getDescription());
        assertEquals(BigDecimal.valueOf(5.00), flower.getOptions().get(0).getPrice());

        assertNotNull(flower.getPrice());
        assertEquals(BigDecimal.valueOf(10.00), flower.getPrice().getAmount());
        assertEquals(Currency.USD, flower.getPrice().getCurrency());
    }


    @Test
    public void testFlowerIdAndInventoryIdMapping() {
        // Arrange
        FlowerRequestModel flowerRequestModel = new FlowerRequestModel();
        flowerRequestModel.setFlowerId("FLOWER001");
        flowerRequestModel.setInventoryId("INV001");
        flowerRequestModel.setCurrency("USD"); // Make sure currency is set here

        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);

        assertEquals("FLOWER001", flower.getFlowersIdentifier().getFlowerNumber());
        assertEquals("INV001", flower.getInventoryIdentifier().getInventoryId());
        assertEquals(Currency.USD, flower.getPrice().getCurrency()); // Check that currency is properly set
    }

    @Test
    public void testOptionsMapping() {
        // Arrange: Creating an empty list of options and a valid currency
        FlowerRequestModel flowerRequestModel = new FlowerRequestModel();
        flowerRequestModel.setOptions(new ArrayList<>()); // Empty options list
        flowerRequestModel.setCurrency("USD");  // Set a valid currency value

        // Act
        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);

        // Assert
        assertNotNull(flower.getOptions());  // Should not be null, but empty
        assertEquals(0, flower.getOptions().size());  // No options in the list
    }

    @Test
    public void testPriceMapping() {
        // Arrange
        FlowerRequestModel flowerRequestModel = new FlowerRequestModel();
        flowerRequestModel.setPrice(BigDecimal.valueOf(10.0));
        flowerRequestModel.setCurrency("USD");

        // Act
        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);

        // Assert
        assertNotNull(flower.getPrice());
        assertEquals(BigDecimal.valueOf(10.0), flower.getPrice().getAmount()); // <- FIXED
        assertEquals(Currency.USD, flower.getPrice().getCurrency());
    }


    @Test
    public void testFlowerStatusMapping() {
        FlowerRequestModel flowerRequestModel = new FlowerRequestModel();

        // Set valid flower status and currency
        String validStatus = "AVAILABLE";
        flowerRequestModel.setFlowerStatus(validStatus);
        flowerRequestModel.setCurrency("USD"); // ✅ Default to avoid NPE

        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);

        assertNotNull(flower);
        assertEquals(Status.AVAILABLE, flower.getStatus());

        // Case 2: Null status, but currency still valid
        flowerRequestModel.setFlowerStatus(null);
        flowerRequestModel.setCurrency("USD"); // ✅ Ensure this is still set

        try {
            flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);
            assertNull(flower.getStatus());  // Ensure status is null if it was passed as null
        } catch (NullPointerException e) {
            fail("NullPointerException should not occur. Please handle null values in requestModelToEntity()");
        }

        // Case 3: Invalid status
        flowerRequestModel.setFlowerStatus("INVALID_STATUS");
        flowerRequestModel.setCurrency("USD"); // ✅ Ensure it's set so it doesn't crash

        try {
            flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);
            fail("Expected IllegalArgumentException when flowerStatus is invalid");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }
    }


    @Test
    public void testInvalidFlowerStatusMapping() {
        // Arrange
        FlowerRequestModel flowerRequestModel = new FlowerRequestModel();
        flowerRequestModel.setFlowerStatus("USED");  // Invalid status
        flowerRequestModel.setCurrency("USD");       // Prevent NullPointerException on other fields

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flowerRequestMapper.requestModelToEntity(flowerRequestModel);
        });

        // Optional: Confirm it fails because of enum issue (more general)
        assertTrue(exception.getMessage().contains("USED"));
    }


    @Test
    public void testEntityToResponseModel() {
        // Arrange
        Flower flower = new Flower();

        // IDs
        FlowersIdentifier flowersIdentifier = new FlowersIdentifier();
        flowersIdentifier.setFlowerNumber("F123");
        flower.setFlowersIdentifier(flowersIdentifier);

        InventoryIdentifier inventoryIdentifier = new InventoryIdentifier();
        inventoryIdentifier.setInventoryId("INV456");
        flower.setInventoryIdentifier(inventoryIdentifier);

        // Basic info
        flower.setFlowerName("Rose");
        flower.setFlowerColor("Red");
        flower.setFlowerCategory("Romantic");
        flower.setStatus(Status.AVAILABLE);
        flower.setStockQuantity(50);
        flower.setSupplierIdentifier("SUP789");

        // Price
        Price price = new Price();
        price.setAmount(BigDecimal.valueOf(9.99));
        price.setCurrency(Currency.USD);
        flower.setPrice(price);

        // Options
        Option option = new Option();
        option.setName("With Vase");
        option.setDescription("Glass vase included");
        option.setPrice(BigDecimal.valueOf(2.50));
        flower.setOptions(List.of(option));

        // Act
        FlowerResponseModel responseModel = flowerResponseMapper.entityToResponseModel(flower);

        // Assert
        assertNotNull(responseModel);
        assertEquals("F123", responseModel.getFlowerId());
        assertEquals("INV456", responseModel.getInventoryId());
        assertEquals("Rose", responseModel.getFlowerName());
        assertEquals("Red", responseModel.getFlowerColor());
        assertEquals("Romantic", responseModel.getFlowerCategory());
        assertEquals("AVAILABLE", responseModel.getFlowerStatus());
        assertEquals(50, responseModel.getStockQuantity());
        assertEquals("SUP789", responseModel.getSupplierIdentifier());
        assertEquals(BigDecimal.valueOf(9.99), responseModel.getPrice());
        assertEquals(Currency.USD.name(), responseModel.getCurrency());

        assertNotNull(responseModel.getOptions());
        assertEquals(1, responseModel.getOptions().size());
        assertEquals("With Vase", responseModel.getOptions().get(0).getName());
    }



    @Test
    public void testEntityListToResponseModelList() {
        // Arrange
        Flower flower1 = new Flower();
        FlowersIdentifier id1 = new FlowersIdentifier();
        id1.setFlowerNumber("F1");
        flower1.setFlowersIdentifier(id1);

        Flower flower2 = new Flower();
        FlowersIdentifier id2 = new FlowersIdentifier();
        id2.setFlowerNumber("F2");
        flower2.setFlowersIdentifier(id2);

        List<Flower> flowers = List.of(flower1, flower2);

        // Act
        List<FlowerResponseModel> responseModels = flowerResponseMapper.entityListToResponseModelList(flowers);

        // Assert
        assertNotNull(responseModels);
        assertEquals(2, responseModels.size());
        assertEquals("F1", responseModels.get(0).getFlowerId());
        assertEquals("F2", responseModels.get(1).getFlowerId());
    }

}
