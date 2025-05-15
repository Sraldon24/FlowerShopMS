package com.champsoft.services.inventory.BusinessLayer;

import com.champsoft.services.inventory.BusinessLayer.Flower.FlowerServiceImpl;
import com.champsoft.services.inventory.Client.SuppliersServiceClient;
import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowerRepository;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowersIdentifier;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlowerServiceImplTest {

    private FlowerRepository flowerRepository;
    private FlowerRequestMapper flowerRequestMapper;
    private FlowerResponseMapper flowerResponseMapper;
    private SuppliersServiceClient suppliersServiceClient;
    private FlowerServiceImpl flowerService;

    @BeforeEach
    void setup() {
        flowerRepository = mock(FlowerRepository.class);
        flowerRequestMapper = mock(FlowerRequestMapper.class);
        flowerResponseMapper = mock(FlowerResponseMapper.class);
        suppliersServiceClient = mock(SuppliersServiceClient.class);
        flowerService = new FlowerServiceImpl(flowerRepository, flowerRequestMapper, flowerResponseMapper, suppliersServiceClient);
    }

    @Test
    void getAllFlowers_shouldReturnList() {
        Flower flower = new Flower();
        when(flowerRepository.findAll()).thenReturn(List.of(flower));
        when(flowerResponseMapper.entityToResponseModel(any())).thenReturn(new FlowerResponseModel());

        List<FlowerResponseModel> result = flowerService.getAllFlowers();
        assertEquals(1, result.size());
    }

    @Test
    void getFlowerById_shouldReturnFlower() {
        String flowerId = "flower123";
        Flower flower = new Flower();
        flower.setFlowersIdentifier(new FlowersIdentifier(flowerId));
        when(flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId)).thenReturn(Optional.of(flower));
        when(flowerResponseMapper.entityToResponseModel(any())).thenReturn(new FlowerResponseModel());

        FlowerResponseModel result = flowerService.getFlowerById(flowerId);
        assertNotNull(result);
    }

    @Test
    void addFlower_shouldSaveAndReturnResponse() {
        FlowerRequestModel request = new FlowerRequestModel();
        Flower flower = new Flower();
        flower.setInventoryIdentifier(new InventoryIdentifier("inv-id"));
        when(flowerRequestMapper.requestModelToEntity(request)).thenReturn(flower);
        when(flowerRepository.save(any())).thenReturn(flower);
        when(flowerResponseMapper.entityToResponseModel(flower)).thenReturn(new FlowerResponseModel());

        FlowerResponseModel result = flowerService.addFlower(request);
        assertNotNull(result);
    }

    @Test
    void updateFlower_shouldUpdateAndReturnResponse() {
        String id = "flower123";
        FlowerRequestModel request = new FlowerRequestModel();
        Flower existing = new Flower();
        existing.setFlowersIdentifier(new FlowersIdentifier(id));
        when(flowerRepository.findByFlowersIdentifier_FlowerNumber(id)).thenReturn(Optional.of(existing));
        when(flowerRequestMapper.requestModelToEntity(request)).thenReturn(existing);
        when(flowerRepository.save(existing)).thenReturn(existing);
        when(flowerResponseMapper.entityToResponseModel(existing)).thenReturn(new FlowerResponseModel());

        FlowerResponseModel result = flowerService.updateFlower(id, request);
        assertNotNull(result);
    }

    @Test
    void deleteFlower_shouldCallRepositoryDelete() {
        String id = "flower123";
        Flower flower = new Flower();
        when(flowerRepository.findByFlowersIdentifier_FlowerNumber(id)).thenReturn(Optional.of(flower));

        flowerService.deleteFlower(id);
        verify(flowerRepository).delete(flower);
    }


    //Exceptions

    @Test
    void getFlowerById_shouldThrowIfNotFound() {
        String flowerId = "non-existent-id";
        when(flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            flowerService.getFlowerById(flowerId);
        });

        assertEquals("Flower not found with ID: " + flowerId, exception.getMessage());
    }


    @Test
    void updateFlower_shouldThrowIfNotFound() {
        String flowerId = "non-existent-id";
        when(flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId))
                .thenReturn(Optional.empty());

        FlowerRequestModel dummyRequest = new FlowerRequestModel();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            flowerService.updateFlower(flowerId, dummyRequest);
        });

        assertEquals("Flower not found with ID: " + flowerId, exception.getMessage());
    }


    @Test
    void deleteFlower_shouldThrowIfNotFound() {
        String flowerId = "non-existent-id";
        when(flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            flowerService.deleteFlower(flowerId);
        });

        assertEquals("Flower not found with ID: " + flowerId, exception.getMessage());
    }
}
