/* ============================
   FlowerController.java
   ============================ */
package com.champsoft.services.inventory.PresentationLayer.Flower;

import com.champsoft.services.inventory.BusinessLayer.Flower.FlowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/flowers")
public class FlowerController {

    private final FlowerService flowerService;

    public FlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    @GetMapping
    public ResponseEntity<List<FlowerResponseModel>> getFlowersWithFilter(@RequestParam Map<String, String> queryParams) {
        return ResponseEntity.ok().body(flowerService.getAllFlowers());
    }

    @GetMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> getFlowerById(@PathVariable String flowerId) {
        return ResponseEntity.ok().body(flowerService.getFlowerById(flowerId));
    }

    @PostMapping
    public ResponseEntity<FlowerResponseModel> addFlower(@RequestBody FlowerRequestModel flowerRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flowerService.addFlower(flowerRequestModel));
    }

    @PutMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> updateFlower(@PathVariable String flowerId,
                                                            @RequestBody FlowerRequestModel flowerRequestModel) {
        return ResponseEntity.ok(flowerService.updateFlower(flowerId, flowerRequestModel));
    }

    @DeleteMapping("/{flowerId}")
    public ResponseEntity<Void> deleteFlower(@PathVariable String flowerId) {
        flowerService.deleteFlower(flowerId);
        return ResponseEntity.noContent().build();
    }
}
