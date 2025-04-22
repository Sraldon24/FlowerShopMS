package com.champsoft.services.inventory.BusinessLayer;

import com.champsoft.services.inventory.BusinessLayer.Inventory.InventoryServiceImpl;
import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowerRepository;
import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryRepository;
import com.champsoft.services.inventory.MapperLayer.Inventory.InventoryRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Inventory.InventoryResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceImplTest {

    private InventoryRepository inventoryRepository;
    private FlowerRepository flowerRepository;
    private InventoryRequestMapper inventoryRequestMapper;
    private InventoryResponseMapper inventoryResponseMapper;
    private InventoryServiceImpl service;

    @BeforeEach
    void setup() {
        inventoryRepository = mock(InventoryRepository.class);
        flowerRepository = mock(FlowerRepository.class);
        inventoryRequestMapper = mock(InventoryRequestMapper.class);
        inventoryResponseMapper = mock(InventoryResponseMapper.class);
        service = new InventoryServiceImpl(inventoryRepository, flowerRepository, inventoryRequestMapper, inventoryResponseMapper);
    }

    @Test
    void getInventories_shouldReturnMappedList() {
        when(inventoryRepository.findAll()).thenReturn(List.of(new Inventory()));
        when(inventoryResponseMapper.entityToResponseModel(any())).thenReturn(new InventoryResponseModel());

        List<InventoryResponseModel> result = service.getInventories();
        assertEquals(1, result.size());
    }

    @Test
    void getInventoryById_shouldReturnInventory() {
        Inventory inventory = new Inventory();
        when(inventoryRepository.findByInventoryIdentifier_InventoryId("id")).thenReturn(Optional.of(inventory));
        when(inventoryResponseMapper.entityToResponseModel(inventory)).thenReturn(new InventoryResponseModel());

        InventoryResponseModel result = service.getInventoryById("id");
        assertNotNull(result);
    }

    @Test
    void addInventory_shouldSaveAndReturn() {
        InventoryRequestModel request = new InventoryRequestModel();
        Inventory inventory = new Inventory();
        when(inventoryRequestMapper.requestModelToEntity(request)).thenReturn(inventory);
        when(inventoryRepository.save(any())).thenReturn(inventory);
        when(inventoryResponseMapper.entityToResponseModel(inventory)).thenReturn(new InventoryResponseModel());

        InventoryResponseModel result = service.addInventory(request);
        assertNotNull(result);
    }

    @Test
    void updateInventory_shouldSaveChanges() {
        String id = "inv-id";
        Inventory inventory = new Inventory();
        InventoryRequestModel request = new InventoryRequestModel();
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(id)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryResponseMapper.entityToResponseModel(inventory)).thenReturn(new InventoryResponseModel());

        InventoryResponseModel result = service.updateInventoryDetails(request, id);
        assertNotNull(result);
    }

    @Test
    void deleteInventory_shouldRemoveAllFlowersThenInventory() {
        String id = "inv-id";
        Inventory inventory = new Inventory();
        List<Flower> flowers = List.of(new Flower());
        when(inventoryRepository.findByInventoryIdentifier_InventoryId(id)).thenReturn(Optional.of(inventory));
        when(flowerRepository.findAllByInventoryIdentifier_InventoryId(id)).thenReturn(flowers);

        service.deleteInventory(id);

        verify(flowerRepository).delete(any());
        verify(inventoryRepository).delete(inventory);
    }



    //Exceptions

    @Test
    void getInventoryById_shouldThrowIfNotFound() {
        when(inventoryRepository.findByInventoryIdentifier_InventoryId("not-found")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getInventoryById("not-found");
        });

        assertEquals("inventory not found with ID: not-found", exception.getMessage());
    }



    @Test
    void updateInventory_shouldThrowIfNotFound() {
        when(inventoryRepository.findByInventoryIdentifier_InventoryId("not-found")).thenReturn(Optional.empty());

        InventoryRequestModel dummyRequest = new InventoryRequestModel();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.updateInventoryDetails(dummyRequest, "not-found");
        });

        assertEquals("inventory not found with ID: not-found", exception.getMessage());
    }


    @Test
    void deleteInventory_shouldThrowIfNotFound() {
        when(inventoryRepository.findByInventoryIdentifier_InventoryId("not-found")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.deleteInventory("not-found");
        });

        assertEquals("inventory not found with ID: not-found", exception.getMessage());
    }
}
