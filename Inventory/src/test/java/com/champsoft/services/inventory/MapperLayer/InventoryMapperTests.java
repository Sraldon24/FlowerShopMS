package com.champsoft.services.inventory.MapperLayer;

import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.MapperLayer.Inventory.InventoryRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Inventory.InventoryResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryResponseModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryMapperTests {
    private final InventoryResponseMapper inventoryResponseMapper = Mappers.getMapper(InventoryResponseMapper.class);

    @Test
    public void testEntityToResponseModel() {
        Inventory inventory = new Inventory();
        InventoryIdentifier inventoryIdentifier = new InventoryIdentifier();
        inventoryIdentifier.setInventoryId("INV123");
        inventory.setInventoryIdentifier(inventoryIdentifier);

        InventoryResponseModel responseModel = inventoryResponseMapper.entityToResponseModel(inventory);

        assertNotNull(responseModel);
        assertEquals("INV123", responseModel.getInventoryId());
    }

    @Test
    public void testEntityToResponseModel_nullInventory() {
        Inventory inventory = null;

        InventoryResponseModel responseModel = inventoryResponseMapper.entityToResponseModel(inventory);

        assertNull(responseModel);
    }

    @Test
    public void testEntityListToResponseModelList() {
        Inventory inventory1 = new Inventory();
        InventoryIdentifier inventoryIdentifier1 = new InventoryIdentifier();
        inventoryIdentifier1.setInventoryId("INV123");
        inventory1.setInventoryIdentifier(inventoryIdentifier1);

        Inventory inventory2 = new Inventory();
        InventoryIdentifier inventoryIdentifier2 = new InventoryIdentifier();
        inventoryIdentifier2.setInventoryId("INV456");
        inventory2.setInventoryIdentifier(inventoryIdentifier2);

        List<Inventory> inventories = Arrays.asList(inventory1, inventory2);

        List<InventoryResponseModel> responseModels = inventoryResponseMapper.entityListToResponseModelList(inventories);

        assertNotNull(responseModels);
        assertEquals(2, responseModels.size());

        InventoryResponseModel responseModel1 = responseModels.get(0);
        assertEquals("INV123", responseModel1.getInventoryId());

        InventoryResponseModel responseModel2 = responseModels.get(1);
        assertEquals("INV456", responseModel2.getInventoryId());
    }

    @Test
    public void testEntityListToResponseModelList_emptyList() {

        List<Inventory> inventories = Collections.emptyList();

        List<InventoryResponseModel> responseModels = inventoryResponseMapper.entityListToResponseModelList(inventories);

        assertNotNull(responseModels);
        assertTrue(responseModels.isEmpty());
    }

    @Test
    public void testEntityListToResponseModelList_nullList() {
        List<Inventory> inventories = null;

        List<InventoryResponseModel> responseModels = inventoryResponseMapper.entityListToResponseModelList(inventories);

        assertNull(responseModels);
    }

    private final InventoryRequestMapper inventoryRequestMapper = Mappers.getMapper(InventoryRequestMapper.class);

    @Test
    public void testRequestModelToEntity() {
        // Arrange
        InventoryRequestModel requestModel = new InventoryRequestModel();

        Inventory inventory = inventoryRequestMapper.requestModelToEntity(requestModel);

        assertNotNull(inventory);
        assertNull(inventory.getId());
        assertNull(inventory.getInventoryIdentifier());
    }

    @Test
    public void testRequestModelToEntity_nullRequestModel() {
        InventoryRequestModel requestModel = null;
        Inventory inventory = inventoryRequestMapper.requestModelToEntity(requestModel);
        assertNull(inventory);
    }

    @Test
    public void testRequestModelToEntity_emptyFields() {
        InventoryRequestModel requestModel = new InventoryRequestModel();
        Inventory inventory = inventoryRequestMapper.requestModelToEntity(requestModel);

        assertNotNull(inventory);
        assertNull(inventory.getId());
        assertNull(inventory.getInventoryIdentifier());

    }
}
