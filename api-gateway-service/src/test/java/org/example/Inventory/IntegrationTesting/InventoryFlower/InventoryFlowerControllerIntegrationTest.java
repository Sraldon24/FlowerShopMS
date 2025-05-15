package org.example.Inventory.IntegrationTesting.InventoryFlower;

import org.example.Inventory.BusinessLayer.InventoryFlower.InventoryFlowerServiceImpl;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.example.Inventory.PresentationLayer.InventoryFlower.InventoryFlowerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = InventoryFlowerController.class)
public class InventoryFlowerControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InventoryFlowerServiceImpl inventoryFlowerService;

    private final FlowerResponseModel sampleFlower = new FlowerResponseModel(
            "flw-9999", "inv-9999", "Rose", "Red", "Romantic", "Available", 100,
            "sup-9999", new BigDecimal("9.99"), "CAD", List.of(), null
    );

    @Test
    public void testGetFlowersInInventory_returnsList() {
        when(inventoryFlowerService.getFlowersInInventory("inv-9999"))
                .thenReturn(List.of(sampleFlower));

        webTestClient.get()
                .uri("/api/v1/inventories/inv-9999/flowers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].flowerId").isEqualTo("flw-9999")
                .jsonPath("$[0].flowerName").isEqualTo("Rose")
                .jsonPath("$[0].price").isEqualTo(9.99);
    }

    @Test
    public void testGetInventoryFlowerById_returnsSingleFlower() {
        when(inventoryFlowerService.getInventoryFlowerById("inv-9999", "flw-9999"))
                .thenReturn(sampleFlower);

        webTestClient.get()
                .uri("/api/v1/inventories/inv-9999/flowers/flw-9999")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.flowerId").isEqualTo("flw-9999")
                .jsonPath("$.flowerColor").isEqualTo("Red")
                .jsonPath("$.stockQuantity").isEqualTo(100);
    }
}