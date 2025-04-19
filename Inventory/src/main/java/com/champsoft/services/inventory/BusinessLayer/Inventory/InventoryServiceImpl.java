package com.champsoft.services.inventory.BusinessLayer.Inventory;

import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowerRepository;
import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryRepository;
import com.champsoft.services.inventory.MapperLayer.Inventory.InventoryRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Inventory.InventoryResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryController;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final FlowerRepository flowerRepository;
    private final InventoryRequestMapper inventoryRequestMapper;
    private final InventoryResponseMapper inventoryResponseMapper;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            FlowerRepository flowerRepository,
            InventoryRequestMapper inventoryRequestMapper,
            InventoryResponseMapper inventoryResponseMapper) {
        this.inventoryRepository = inventoryRepository;
        this.flowerRepository = flowerRepository;
        this.inventoryRequestMapper = inventoryRequestMapper;
        this.inventoryResponseMapper = inventoryResponseMapper;
    }

    @Override
    public List<InventoryResponseModel> getInventories() {
        List<Inventory> inventories = inventoryRepository.findAll();

        return inventories.stream()
                .map(inventory -> {
                    InventoryResponseModel model = inventoryResponseMapper.entityToResponseModel(inventory);
                    String id = model.getInventoryId();

                    // Add HATEOAS links: get by id and delete by id
                    model.add(linkTo(methodOn(InventoryController.class).getInventoryById(id)).withRel("get-inventory-by-id"));
                    model.add(linkTo(methodOn(InventoryController.class).deleteInventory(id)).withRel("delete-inventory-by-id"));

                    return model;
                })
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponseModel getInventoryById(String inventoryId) {
        Inventory inventory = inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)
                .orElseThrow(() -> new RuntimeException("inventory not found with ID: " + inventoryId));

        return inventoryResponseMapper.entityToResponseModel(inventory);
    }

    @Override
    public InventoryResponseModel addInventory(InventoryRequestModel inventoryRequestModel) {
        Inventory inventory = inventoryRequestMapper.requestModelToEntity(inventoryRequestModel);

        if (inventory.getInventoryIdentifier() == null) {
            inventory.setInventoryIdentifier(new InventoryIdentifier());
        }

        inventory.getInventoryIdentifier().generateIfMissing();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryResponseMapper.entityToResponseModel(savedInventory);
    }


    @Override
    public InventoryResponseModel updateInventoryDetails(InventoryRequestModel inventoryRequestModel, String inventoryId) {
        Inventory existingInventory = inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)
                .orElseThrow(() -> new RuntimeException("inventory not found with ID: " + inventoryId));
        existingInventory.setType(inventoryRequestModel.getType());
        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return inventoryResponseMapper.entityToResponseModel(updatedInventory);
    }

    @Override
    public void deleteInventory(String inventoryId) {
        Inventory existingInventory = inventoryRepository.findByInventoryIdentifier_InventoryId(inventoryId)
                .orElseThrow(() -> new RuntimeException("inventory not found with ID: " + inventoryId));

        List<Flower> flowers = flowerRepository.findAllByInventoryIdentifier_InventoryId(inventoryId);
        for (Flower flower : flowers) {
            flowerRepository.delete(flower);
        }

        inventoryRepository.delete(existingInventory);
    }
}