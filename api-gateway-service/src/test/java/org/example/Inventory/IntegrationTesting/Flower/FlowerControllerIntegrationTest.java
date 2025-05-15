package org.example.Inventory.IntegrationTesting.Flower;

import org.example.Inventory.BusinessLayer.Flower.FlowerServiceImpl;
import org.example.Inventory.PresentationLayer.Flower.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FlowerController.class)
public class FlowerControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FlowerServiceImpl flowerService;

    private final FlowerResponseModel sampleResponse = new FlowerResponseModel(
            "flw-001",
            "inv-001",
            "Rose",
            "Red",
            "Valentine",
            "Fresh",
            20,
            "sup-001",
            new BigDecimal("12.50"),
            "CAD",
            List.of(),
            null
    );

    @Test
    public void testGetAllFlowers_returnsList() {
        when(flowerService.getFlowers()).thenReturn(List.of(sampleResponse));

        webTestClient.get()
                .uri("/api/v1/flowers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].flowerId").isEqualTo("flw-001")
                .jsonPath("$[0].flowerColor").isEqualTo("Red");
    }

    @Test
    public void testGetFlowerById_returnsOne() {
        when(flowerService.getFlowerById("flw-001")).thenReturn(sampleResponse);

        webTestClient.get()
                .uri("/api/v1/flowers/flw-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.flowerName").isEqualTo("Rose")
                .jsonPath("$.price").isEqualTo(12.50);
    }

    @Test
    public void testAddFlower_returnsCreated() {
        FlowerRequestModel request = new FlowerRequestModel(
                "flw-001",
                "inv-001",
                "Rose",
                "Red",
                "Valentine",
                "Fresh",
                20,
                "sup-001",
                new BigDecimal("12.50"),
                "CAD",
                List.of()
        );

        when(flowerService.addFlower(any(FlowerRequestModel.class))).thenReturn(sampleResponse);

        webTestClient.post()
                .uri("/api/v1/flowers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.flowerCategory").isEqualTo("Valentine")
                .jsonPath("$.flowerId").isEqualTo("flw-001");
    }

    @Test
    public void testUpdateFlower_returnsUpdated() {
        FlowerRequestModel request = new FlowerRequestModel(
                "flw-001",
                "inv-001",
                "Rose",
                "Red",
                "Valentine",
                "Fresh",
                25,
                "sup-001",
                new BigDecimal("13.99"),
                "CAD",
                List.of()
        );

        when(flowerService.updateFlower(anyString(), any(FlowerRequestModel.class))).thenReturn(sampleResponse);

        webTestClient.put()
                .uri("/api/v1/flowers/flw-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.stockQuantity").isEqualTo(20)
                .jsonPath("$.currency").isEqualTo("CAD");
    }

    @Test
    public void testDeleteFlower_returnsOk() {
        when(flowerService.deleteFlower("flw-001")).thenReturn("Flower deleted successfully.");

        webTestClient.delete()
                .uri("/api/v1/flowers/flw-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Flower deleted successfully.");
    }
}
