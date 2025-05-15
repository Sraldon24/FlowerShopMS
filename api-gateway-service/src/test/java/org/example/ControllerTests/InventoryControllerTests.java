//package org.example.ControllerTests;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.Inventory.BusinessLayer.Inventory.InventoryServiceImpl;
//import org.example.Inventory.PresentationLayer.Inventory.InventoryController;
//import org.example.Sales.PresentationLayer.inventorydtos.InventoryResponseModel;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class InventoryControllerUnitTest {
//
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private InventoryController inventoryController;
//
//    @Mock
//    private InventoryServiceImpl inventoryService; // Mock the service, not the client
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void getInventories_positive() throws Exception {
//        // Arrange
//        List<InventoryResponseModel> inventories = Collections.singletonList(
//                new InventoryResponseModel("inv123", "flower")
//        );
//        when(inventoryService.getInventories()).thenReturn(inventories);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/inventories")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].inventoryId").value("inv123"))
//                .andExpect(jsonPath("$[0].type").value("flower"));
//    }
//
//    @Test
//    void getInventoryById_positive() throws Exception {
//        // Arrange
//        String inventoryId = "inv123";
//        InventoryResponseModel inventory = new InventoryResponseModel(inventoryId, "flower");
//        when(inventoryService.getInventoryById(inventoryId)).thenReturn(inventory);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/inventories/" + inventoryId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.inventoryId").value(inventoryId))
//                .andExpect(jsonPath("$.type").value("flower"));
//    }
//
//    @Test
//    void getInventoryById_negative_notFound() throws Exception {
//        // Arrange
//        String inventoryId = "nonExistent";
//        when(inventoryService.getInventoryById(inventoryId)).thenReturn(null);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/inventories/" + inventoryId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()); // Controller returns ResponseEntity.ok(null) which is 200
//    }
//
//    // Add more tests for other controller methods (addInventory, updateInventory, deleteInventory)
//}