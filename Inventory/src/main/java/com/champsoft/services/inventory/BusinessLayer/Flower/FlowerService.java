/* ===========================
   FlowerService.java
   =========================== */
package com.champsoft.services.inventory.BusinessLayer.Flower;

import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;

import java.util.List;

public interface FlowerService {
    List<FlowerResponseModel> getAllFlowers();

    FlowerResponseModel getFlowerById(String flowerId);

    FlowerResponseModel addFlower(FlowerRequestModel flowerRequestModel);

    FlowerResponseModel updateFlower(String flowerId, FlowerRequestModel flowerRequestModel);

    void deleteFlower(String flowerId);
}
