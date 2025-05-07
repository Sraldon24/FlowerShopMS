package org.example.Inventory.PresentationLayer.Flower;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.Inventory.BusinessLayer.Flower.FlowerServiceImpl;
import org.example.Payment.Utils.InvalidInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flowers")
@RequiredArgsConstructor
@Slf4j
public class FlowerController {

    private final FlowerServiceImpl flowerService;
    private static final int UUID_SIZE = 17; // ex: flw-123456789abcd

    @GetMapping
    public ResponseEntity<List<FlowerResponseModel>> getFlowers() {
        return ResponseEntity.ok(flowerService.getFlowers());
    }

    @GetMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> getFlowerById(@PathVariable String flowerId) {
        return ResponseEntity.ok(flowerService.getFlowerById(flowerId));
    }

    @PostMapping
    public ResponseEntity<FlowerResponseModel> addFlower(@RequestBody FlowerRequestModel model) {
        return ResponseEntity.status(201).body(flowerService.addFlower(model));
    }

    @PutMapping("/{flowerId}")
    public ResponseEntity<FlowerResponseModel> updateFlower(@PathVariable String flowerId,
                                                            @RequestBody FlowerRequestModel model) {
        if (flowerId.length() != UUID_SIZE) throw new InvalidInputException("Invalid flower ID");
        return ResponseEntity.ok(flowerService.updateFlower(flowerId, model));
    }

    @DeleteMapping("/{flowerId}")
    public ResponseEntity<String> deleteFlower(@PathVariable String flowerId) {
        return ResponseEntity.ok(flowerService.deleteFlower(flowerId));
    }
}
