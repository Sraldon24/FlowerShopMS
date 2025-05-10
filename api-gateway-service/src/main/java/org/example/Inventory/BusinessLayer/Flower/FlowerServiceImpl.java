package org.example.Inventory.BusinessLayer.Flower;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.DomainClientLayer.Flower.FlowerServiceClient;
import org.example.Inventory.PresentationLayer.Flower.FlowerController;
import org.example.Inventory.PresentationLayer.Flower.FlowerRequestModel;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlowerServiceImpl {

    private final FlowerServiceClient flowerServiceClient;

    public List<FlowerResponseModel> getFlowers() {
        List<FlowerResponseModel> flowers = flowerServiceClient.getFlowers();

        flowers.forEach(f -> {
            f.add(linkTo(methodOn(FlowerController.class).getFlowerById(f.getFlowerId())).withRel("get-by-id"));
            f.add(linkTo(methodOn(FlowerController.class).deleteFlower(f.getFlowerId())).withRel("delete"));
        });

        return flowers;
    }

    public FlowerResponseModel getFlowerById(String id) {
        return flowerServiceClient.getFlowerById(id);
    }

    public FlowerResponseModel addFlower(FlowerRequestModel model) {
        return flowerServiceClient.addFlower(model);
    }

    public FlowerResponseModel updateFlower(String id, FlowerRequestModel model) {
        return flowerServiceClient.updateFlower(id, model);
    }

    public String deleteFlower(String id) {
        return flowerServiceClient.deleteFlower(id);
    }
}
