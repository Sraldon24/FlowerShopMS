/* ===========================
   FlowerServiceImpl.java
   =========================== */
package com.champsoft.services.inventory.BusinessLayer.Flower;

import com.champsoft.services.inventory.Client.SuppliersServiceClient;
import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowerRepository;
import com.champsoft.services.inventory.DataLayer.Flowers.FlowersIdentifier;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerRequestMapper;
import com.champsoft.services.inventory.MapperLayer.Flower.FlowerResponseMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowerServiceImpl implements FlowerService {

    private final FlowerRepository flowerRepository;
    private final FlowerRequestMapper flowerRequestMapper;
    private final FlowerResponseMapper flowerResponseMapper;
    private final SuppliersServiceClient suppliersServiceClient;

    public FlowerServiceImpl(
            FlowerRepository flowerRepository,
            FlowerRequestMapper flowerRequestMapper,
            FlowerResponseMapper flowerResponseMapper,
            SuppliersServiceClient suppliersServiceClient
    ) {
        this.flowerRepository = flowerRepository;
        this.flowerRequestMapper = flowerRequestMapper;
        this.flowerResponseMapper = flowerResponseMapper;
        this.suppliersServiceClient = suppliersServiceClient;
    }

    @Override
    public List<FlowerResponseModel> getAllFlowers() {
        return flowerRepository.findAll().stream()
                .map(flower -> {
                    FlowerResponseModel response = flowerResponseMapper.entityToResponseModel(flower);
                    response.setSupplier(suppliersServiceClient.getSupplierBySupplierId(flower.getSupplierIdentifier()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public FlowerResponseModel getFlowerById(String flowerId) {
        Flower flower = flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found with ID: " + flowerId));
        FlowerResponseModel response = flowerResponseMapper.entityToResponseModel(flower);
        response.setSupplier(suppliersServiceClient.getSupplierBySupplierId(flower.getSupplierIdentifier()));
        return response;
    }

    @Override
    public FlowerResponseModel addFlower(FlowerRequestModel flowerRequestModel) {
        Flower flower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);

        if (flower.getFlowersIdentifier() == null) {
            flower.setFlowersIdentifier(new FlowersIdentifier());
        }
        flower.getFlowersIdentifier().generateIfMissing();

        if (flower.getInventoryIdentifier() == null) {
            flower.setInventoryIdentifier(new InventoryIdentifier(flowerRequestModel.getInventoryId()));
        }

        Flower savedFlower = flowerRepository.save(flower);
        return flowerResponseMapper.entityToResponseModel(savedFlower);
    }

    @Override
    public FlowerResponseModel updateFlower(String flowerId, FlowerRequestModel flowerRequestModel) {
        Flower existingFlower = flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found with ID: " + flowerId));

        Flower updatedFlower = flowerRequestMapper.requestModelToEntity(flowerRequestModel);
        updatedFlower.setId(existingFlower.getId());
        updatedFlower.getFlowersIdentifier()
                .setFlowerNumber(existingFlower.getFlowersIdentifier().getFlowerNumber());

        Flower savedFlower = flowerRepository.save(updatedFlower);
        return flowerResponseMapper.entityToResponseModel(savedFlower);
    }

    @Override
    public void deleteFlower(String flowerId) {
        Flower flower = flowerRepository.findByFlowersIdentifier_FlowerNumber(flowerId)
                .orElseThrow(() -> new RuntimeException("Flower not found with ID: " + flowerId));
        flowerRepository.delete(flower);
    }
}
