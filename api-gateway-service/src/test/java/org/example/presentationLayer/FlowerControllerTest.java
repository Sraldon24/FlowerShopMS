package org.example.presentationLayer;

import org.example.Inventory.BusinessLayer.Flower.FlowerServiceImpl;
import org.example.Inventory.PresentationLayer.Flower.FlowerController;
import org.example.Inventory.PresentationLayer.Flower.FlowerRequestModel;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FlowerController.class)
@Import(FlowerController.class)
public class FlowerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FlowerServiceImpl flowerService;

    private FlowerResponseModel sampleFlower;

    @BeforeEach
    void setup() {
        sampleFlower = new FlowerResponseModel();
        sampleFlower.setFlowerId("flower-001");
        sampleFlower.setFlowerName("Rose");
        sampleFlower.setFlowerColor("Red");
    }

    @Test
    void testGetAllFlowers() {
        when(flowerService.getFlowers()).thenAnswer(invocation -> Collections.singletonList(sampleFlower));

        webTestClient.get().uri("/api/v1/flowers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FlowerResponseModel.class)
                .hasSize(1);
    }

    @Test
    void testGetFlowerById() {
        when(flowerService.getFlowerById("flower-001")).thenAnswer(invocation -> sampleFlower);

        webTestClient.get().uri("/api/v1/flowers/flower-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FlowerResponseModel.class)
                .isEqualTo(sampleFlower);
    }


    @Test
    void testAddFlower() {
        FlowerRequestModel request = new FlowerRequestModel();
        request.setFlowerName("Tulip");
        request.setFlowerColor("Yellow");

        when(flowerService.addFlower(any(FlowerRequestModel.class))).thenReturn(sampleFlower);

        webTestClient.post().uri("/api/v1/flowers")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FlowerResponseModel.class)
                .isEqualTo(sampleFlower);
    }
    @Test
    void testUpdateFlower() {
        FlowerRequestModel updateRequest = new FlowerRequestModel();
        updateRequest.setFlowerName("Sunflower");
        updateRequest.setFlowerColor("Yellow");

        when(flowerService.updateFlower("flower-001", updateRequest)).thenAnswer(invocation -> sampleFlower);

        webTestClient.put().uri("/api/v1/flowers/flower-001")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FlowerResponseModel.class)
                .isEqualTo(sampleFlower);
    }

    @Test
    void testDeleteFlower() {
        when(flowerService.deleteFlower("flower-001")).thenReturn("Deleted");

        webTestClient.delete().uri("/api/v1/flowers/flower-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Deleted");
    }
}
