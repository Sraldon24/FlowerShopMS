package com.champsoft.services.inventory.BusinessLayer;

import com.champsoft.services.inventory.BusinessLayer.Inventory_Flower.InventoryFlowerServiceImpl;
import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowerRepository;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryFlowerServiceImplTest {

    private FlowerRepository flowerRepository;
    private FlowerResponseMapper flowerResponseMapper;
    private FlowerRequestMapper flowerRequestMapper;
    private InventoryFlowerServiceImpl service;

    @BeforeEach
    void setup() {
        flowerRepository = mock(FlowerRepository.class);
        flowerResponseMapper = mock(FlowerResponseMapper.class);
        flowerRequestMapper = mock(FlowerRequestMapper.class);
        service = new InventoryFlowerServiceImpl(flowerRepository, flowerResponseMapper, flowerRequestMapper);
    }

    @Test
    void getFlowersInInventory_shouldReturnList() {
        when(flowerRepository.findByInventoryIdentifier_InventoryId("inv-id")).thenReturn(List.of(new Flower()));
        when(flowerResponseMapper.entityToResponseModel(any())).thenReturn(new FlowerResponseModel());

        List<FlowerResponseModel> result = service.getFlowersInInventory("inv-id");
        assertEquals(1, result.size());
    }

    @Test
    void getInventoryFlowerById_shouldReturnFlower() {
        String invId = "inv-id", flowerId = "flower-id";
        Flower flower = new Flower();
        when(flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber(invId, flowerId))
                .thenReturn(Optional.of(flower));
        when(flowerResponseMapper.entityToResponseModel(flower)).thenReturn(new FlowerResponseModel());

        FlowerResponseModel result = service.getInventoryFlowerById(invId, flowerId);
        assertNotNull(result);
    }

    @Test
    void addFlowerToInventory_shouldReturnSavedFlower() {
        FlowerRequestModel request = new FlowerRequestModel();
        Flower flower = new Flower();
        when(flowerRequestMapper.requestModelToEntity(request)).thenReturn(flower);
        when(flowerRepository.save(flower)).thenReturn(flower);
        when(flowerResponseMapper.entityToResponseModel(flower)).thenReturn(new FlowerResponseModel());

        FlowerResponseModel result = service.addFlowerToInventory("inv-id", request);
        assertNotNull(result);
    }

    @Test
    void updateFlowerInInventory_shouldUpdateFields() {
        FlowerRequestModel request = new FlowerRequestModel();
        request.setFlowerName("name");
        request.setFlowerColor("red");
        request.setFlowerCategory("roses");
        request.setCurrency("CAD");
        request.setPrice(BigDecimal.valueOf(12.0)); // ✅ fixed type

        Flower flower = new Flower();
        when(flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber("inv-id", "flower-id"))
                .thenReturn(Optional.of(flower));
        when(flowerRepository.save(flower)).thenReturn(flower);
        when(flowerResponseMapper.entityToResponseModel(flower)).thenReturn(new FlowerResponseModel());

        FlowerResponseModel result = service.updateFlowerInInventory("inv-id", "flower-id", request);
        assertNotNull(result);
    }

    @Test
    void removeFlowerFromInventory_shouldDeleteFlower() {
        Flower flower = new Flower();
        when(flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber("inv-id", "flower-id"))
                .thenReturn(Optional.of(flower));

        service.removeFlowerFromInventory("inv-id", "flower-id");
        verify(flowerRepository).delete(flower);
    }


    //Exceptions

    @Test
    void getInventoryFlowerById_shouldThrowIfNotFound() {
        when(flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber("inv-id", "flower-id"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getInventoryFlowerById("inv-id", "flower-id");
        });

        assertEquals("Flower not found in inventory inv-id", exception.getMessage());
    }


    @Test
    void updateFlowerInInventory_shouldThrowIfNotFound() {
        FlowerRequestModel request = new FlowerRequestModel();

        when(flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber("inv-id", "flower-id"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.updateFlowerInInventory("inv-id", "flower-id", request);
        });

        assertEquals("Flower not found in inventory inv-id", exception.getMessage());
    }



    @Test
    void removeFlowerFromInventory_shouldThrowIfNotFound() {
        when(flowerRepository.findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber("inv-id", "flower-id"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.removeFlowerFromInventory("inv-id", "flower-id");
        });

        assertEquals("Flower not found in inventory inv-id", exception.getMessage());
    }
}