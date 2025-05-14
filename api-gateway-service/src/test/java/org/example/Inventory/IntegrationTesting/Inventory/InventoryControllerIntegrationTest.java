package org.example.Inventory.IntegrationTesting.Inventory;

import org.example.Inventory.BusinessLayer.Inventory.InventoryServiceImpl;
import org.example.Inventory.PresentationLayer.Inventory.InventoryController;
import org.example.Inventory.PresentationLayer.Inventory.InventoryRequestModel;
import org.example.Inventory.PresentationLayer.Inventory.InventoryResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = InventoryController.class)
public class InventoryControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InventoryServiceImpl inventoryService;

    @Test
    public void testGetInventories_returnsList() {
        InventoryResponseModel mockInventory = new InventoryResponseModel("inv-001", "TOOLS");

        when(inventoryService.getInventories()).thenReturn(List.of(mockInventory));

        webTestClient.get()
                .uri("/api/v1/inventories")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].inventoryId").isEqualTo("inv-001")
                .jsonPath("$[0].type").isEqualTo("TOOLS");
    }

    @Test
    public void testGetInventoryById_returnsSingle() {
        InventoryResponseModel mockInventory = new InventoryResponseModel("inv-002", "FLOWERS");

        when(inventoryService.getInventoryById("inv-002")).thenReturn(mockInventory);

        webTestClient.get()
                .uri("/api/v1/inventories/inv-002")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.inventoryId").isEqualTo("inv-002")
                .jsonPath("$.type").isEqualTo("FLOWERS");
    }

    @Test
    public void testAddInventory_returnsCreated() {
        InventoryRequestModel request = new InventoryRequestModel("POTS");
        InventoryResponseModel response = new InventoryResponseModel("inv-003", "POTS");

        when(inventoryService.addInventory(any(InventoryRequestModel.class))).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.inventoryId").isEqualTo("inv-003")
                .jsonPath("$.type").isEqualTo("POTS");
    }

    @Test
    public void testUpdateInventory_returnsUpdated() {
        InventoryRequestModel request = new InventoryRequestModel("UPDATED_TYPE");
        InventoryResponseModel response = new InventoryResponseModel("inv-004", "UPDATED_TYPE");

        when(inventoryService.updateInventory(anyString(), any(InventoryRequestModel.class))).thenReturn(response);

        webTestClient.put()
                .uri("/api/v1/inventories/inv-004")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.inventoryId").isEqualTo("inv-004")
                .jsonPath("$.type").isEqualTo("UPDATED_TYPE");
    }

    @Test
    public void testDeleteInventory_returnsSuccessMessage() {
        when(inventoryService.deleteInventory("inv-999")).thenReturn("Inventory deleted successfully.");

        webTestClient.delete()
                .uri("/api/v1/inventories/inv-999")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Inventory deleted successfully.");
    }
}
