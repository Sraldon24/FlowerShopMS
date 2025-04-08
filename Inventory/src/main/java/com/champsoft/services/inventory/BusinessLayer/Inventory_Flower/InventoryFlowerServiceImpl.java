/* ========================================
   InventoryFlowerServiceImpl.java
   ======================================== */
package com.champsoft.services.inventory.BusinessLayer.Inventory_Flower;

import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowerRepository;
import com.champsoft.services.inventory.DataLayer.Flowers.Price;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import com.champsoft.services.utils.Currency;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryFlowerServiceImpl implements InventoryFlowerService {

    private final FlowerRepository flowerRepository;
    private final FlowerResponseMapper flowerResponseMapper;
    private final FlowerRequestMapper flowerRequestMapper;

    public InventoryFlowerServiceImpl(
            FlowerRepository flowerRepository,
            FlowerResponseMapper flowerResponseMapper,
            FlowerRequestMapper flowerRequestMapper
    ) {
        this.flowerRepository = flowerRepository;
        this.flowerResponseMapper = flowerResponseMapper;
        this.flowerRequestMapper = flowerRequestMapper;
    }

    @Override
    public List<FlowerResponseModel> getFlowersInInventory(String inventoryId) {
        List<Flower> flowers = flowerRepository.findByInventoryIdentifier_InventoryId(inventoryId);
        return flowers.stream()
                .map(flowerResponseMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public FlowerResponseModel getInventoryFlowerById(String inventoryId, String flowerId) {
        Flower flower = flowerRepository
                .findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber(inventoryId, flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found in inventory " + inventoryId));
        return flowerResponseMapper.entityToResponseModel(flower);
    }

    @Override
    public FlowerResponseModel addFlowerToInventory(String inventoryId, FlowerRequestModel flowerRequestModel) {
        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);
        flower.setInventoryIdentifier(new InventoryIdentifier(inventoryId));
        Flower savedFlower = flowerRepository.save(flower);
        return flowerResponseMapper.entityToResponseModel(savedFlower);
    }

    @Override
    public FlowerResponseModel updateFlowerInInventory(String inventoryId, String flowerId, FlowerRequestModel flowerRequestModel) {
        Flower existingFlower = flowerRepository
                .findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber(inventoryId, flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found in inventory " + inventoryId));

        // Updating newly added fields
        existingFlower.setFlowerName(flowerRequestModel.getFlowerName());
        existingFlower.setFlowerColor(flowerRequestModel.getFlowerColor());
        existingFlower.setFlowerCategory(flowerRequestModel.getFlowerCategory());


        // Updating price details
        Price price = new Price();
        price.setCurrency(Currency.valueOf(flowerRequestModel.getCurrency()));
        price.setAmount(flowerRequestModel.getPrice());
        existingFlower.setPrice(price);

        // Save updated flower and return response
        Flower updatedFlower = flowerRepository.save(existingFlower);
        return flowerResponseMapper.entityToResponseModel(updatedFlower);
    }

    @Override
    public void removeFlowerFromInventory(String inventoryId, String flowerId) {
        Flower flower = flowerRepository
                .findByInventoryIdentifier_InventoryIdAndFlowersIdentifier_FlowerNumber(inventoryId, flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found in inventory " + inventoryId));
        flowerRepository.delete(flower);
    }
}
