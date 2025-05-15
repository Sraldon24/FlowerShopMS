package com.champsoft.services.inventory.PresentationLayer;


import com.champsoft.services.inventory.BusinessLayer.Flower.FlowerService;
import com.champsoft.services.inventory.BusinessLayer.Inventory.InventoryService;
import com.champsoft.services.inventory.BusinessLayer.Inventory_Flower.InventoryFlowerService;
import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerController;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryController;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryResponseModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory_Flower.InventoryFlowerController;
import com.champsoft.services.inventory.utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ControllerTests {

    @InjectMocks
    private InventoryController inventoryController;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private FlowerService flowerService;

    @InjectMocks
    private InventoryFlowerController inventoryFlowerController;

    @Mock
    private InventoryFlowerService inventoryFlowerService;

    @InjectMocks
    private FlowerController flowerController;

    private FlowerRequestModel flowerRequestModel;
    private FlowerResponseModel flowerResponseModel;

    private InventoryResponseModel inventoryResponseModel;
    private InventoryRequestModel inventoryRequestModel;
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventoryResponseModel = new InventoryResponseModel();
        inventoryResponseModel.setInventoryId("123");


        inventoryRequestModel = new InventoryRequestModel();

        inventory = new Inventory();
        inventory.setId(123);

        flowerRequestModel = new FlowerRequestModel();
        flowerRequestModel.setFlowerName("Tulip");
        flowerRequestModel.setFlowerColor("Yellow");
        flowerRequestModel.setStockQuantity(30);

        flowerResponseModel = new FlowerResponseModel();
        flowerResponseModel.setFlowerName("Tulip");
        flowerResponseModel.setFlowerColor("Yellow");
        flowerResponseModel.setStockQuantity(30);
    }

    @Test
    public void testGetInventories() {
        // Arrange
        when(inventoryService.getInventories()).thenReturn(List.of(inventoryResponseModel));

        // Act
        ResponseEntity<CollectionModel<InventoryResponseModel>> response = inventoryController.getInventories();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(inventoryService, times(1)).getInventories();
    }

    @Test
    public void testGetInventoryById() {
        // Arrange
        when(inventoryService.getInventoryById("123")).thenReturn(inventoryResponseModel);

        // Act
        ResponseEntity<InventoryResponseModel> response = inventoryController.getInventoryById("123");

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(inventoryResponseModel, response.getBody());
        verify(inventoryService, times(1)).getInventoryById("123");
    }
    @Test
    void testGetInventoryById_NotFound() {
        when(inventoryService.getInventoryById("invalid-id"))
                .thenThrow(new NotFoundException("Inventory not found"));

        assertThrows(NotFoundException.class, () -> inventoryController.getInventoryById("invalid-id"));
        verify(inventoryService, times(1)).getInventoryById("invalid-id");
    }

    @Test
    public void testAddInventory() {
        // Arrange
        when(inventoryService.addInventory(inventoryRequestModel)).thenReturn(inventoryResponseModel);

        // Act
        ResponseEntity<InventoryResponseModel> response = inventoryController.addInventory(inventoryRequestModel);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(inventoryResponseModel, response.getBody());
        verify(inventoryService, times(1)).addInventory(inventoryRequestModel);
    }

    @Test
    public void testUpdateInventory() {
        // Arrange
        when(inventoryService.updateInventoryDetails(inventoryRequestModel, "123")).thenReturn(inventoryResponseModel);

        // Act
        ResponseEntity<InventoryResponseModel> response = inventoryController.updateInventory("123", inventoryRequestModel);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(inventoryResponseModel, response.getBody());
        verify(inventoryService, times(1)).updateInventoryDetails(inventoryRequestModel, "123");
    }

    @Test
    public void testDeleteInventory() {
        // Arrange
        doNothing().when(inventoryService).deleteInventory("123");

        // Act
        ResponseEntity<Void> response = inventoryController.deleteInventory("123");

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(inventoryService, times(1)).deleteInventory("123");
    }





    @Test
    void testGetFlowersInInventory() {
        when(inventoryFlowerService.getFlowersInInventory("inv1")).thenReturn(List.of(flowerResponseModel));

        ResponseEntity<List<FlowerResponseModel>> response = inventoryFlowerController.getFlowersInInventory("inv1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(inventoryFlowerService, times(1)).getFlowersInInventory("inv1");
    }

    @Test
    void testGetFlowerById() {
        when(inventoryFlowerService.getInventoryFlowerById("inv1", "fl1")).thenReturn(flowerResponseModel);

        ResponseEntity<FlowerResponseModel> response = inventoryFlowerController.getFlowerById("inv1", "fl1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tulip", response.getBody().getFlowerName());
        verify(inventoryFlowerService, times(1)).getInventoryFlowerById("inv1", "fl1");
    }

    @Test
    void testAddFlower() {
        when(inventoryFlowerService.addFlowerToInventory("inv1", flowerRequestModel)).thenReturn(flowerResponseModel);

        ResponseEntity<FlowerResponseModel> response = inventoryFlowerController.addFlower("inv1", flowerRequestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tulip", response.getBody().getFlowerName());
        verify(inventoryFlowerService, times(1)).addFlowerToInventory("inv1", flowerRequestModel);
    }

    @Test
    void testUpdateFlower() {
        when(inventoryFlowerService.updateFlowerInInventory("inv1", "fl1", flowerRequestModel)).thenReturn(flowerResponseModel);

        ResponseEntity<FlowerResponseModel> response = inventoryFlowerController.updateFlower("inv1", "fl1", flowerRequestModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tulip", response.getBody().getFlowerName());
        verify(inventoryFlowerService, times(1)).updateFlowerInInventory("inv1", "fl1", flowerRequestModel);
    }

    @Test
    void testRemoveFlowerFromInventory() {
        doNothing().when(inventoryFlowerService).removeFlowerFromInventory("inv1", "fl1");

        ResponseEntity<Void> response = inventoryFlowerController.removeFlowerFromInventory("inv1", "fl1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(inventoryFlowerService, times(1)).removeFlowerFromInventory("inv1", "fl1");
    }
//    @Test
//    void testGetFlowersWithFilter() {
//        when(flowerService.getAllFlowers()).thenReturn(List.of(flowerResponseModel));
//
//        ResponseEntity<List<FlowerResponseModel>> response = flowerController.getFlowersWithFilter(Map.of("color", "Yellow"));
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(1, response.getBody().size());
//        assertEquals("Tulip", response.getBody().get(0).getFlowerName());
//        verify(flowerService, times(1)).getAllFlowers();
//    }

    @Test
    void testGetFlowerById_FlowerController() {
        when(flowerService.getFlowerById("fl1")).thenReturn(flowerResponseModel);

        ResponseEntity<FlowerResponseModel> response = flowerController.getFlowerById("fl1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tulip", response.getBody().getFlowerName());
        verify(flowerService, times(1)).getFlowerById("fl1");
    }

    @Test
    void testAddFlower_FlowerController() {
        when(flowerService.addFlower(flowerRequestModel)).thenReturn(flowerResponseModel);

        ResponseEntity<FlowerResponseModel> response = flowerController.addFlower(flowerRequestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tulip", response.getBody().getFlowerName());
        verify(flowerService, times(1)).addFlower(flowerRequestModel);
    }

    @Test
    void testUpdateFlower_FlowerController() {
        when(flowerService.updateFlower("fl1", flowerRequestModel)).thenReturn(flowerResponseModel);

        ResponseEntity<FlowerResponseModel> response = flowerController.updateFlower("fl1", flowerRequestModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tulip", response.getBody().getFlowerName());
        verify(flowerService, times(1)).updateFlower("fl1", flowerRequestModel);
    }

    @Test
    void testDeleteFlower_FlowerController() {
        doNothing().when(flowerService).deleteFlower("fl1");

        ResponseEntity<Void> response = flowerController.deleteFlower("fl1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(flowerService, times(1)).deleteFlower("fl1");
    }

}
