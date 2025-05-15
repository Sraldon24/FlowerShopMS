//package org.example.ControllerTests;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.ApiGatewayServiceApplication; // Import your main application class
//import org.example.Inventory.DomainClientLayer.Flower.FlowerServiceClient;
//import org.example.Inventory.DomainClientLayer.Inventory.InventoryServiceClient;
//import org.example.Sales.PresentationLayer.PurchaseController;
//import org.example.Sales.DomainClientLayer.PurchaseServiceClient;
//import org.example.Payment.DomainClientLayer.PaymentServiceClient;
//import org.example.Sales.BusinessLayer.PurchaseServiceImpl;
//import org.example.Suppliers.DomainClientLayer.SupplierServiceClient;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = ApiGatewayServiceApplication.class) // Explicitly specify the application class
//@TestPropertySource(properties = {
//        "app.inventory-service.host=http://localhost",
//        "app.inventory-service.port=8081"
//})
//public class IntegrationTests {
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private PurchaseServiceClient purchaseServiceClient;
//    @MockBean
//    private PaymentServiceClient paymentServiceClient;
//    @MockBean
//    private FlowerServiceClient flowerServiceClient;
//    @MockBean
//    private InventoryServiceClient inventoryServiceClient;
//    @MockBean
//    private PurchaseServiceImpl purchaseService;
//    @MockBean
//    private SupplierServiceClient supplierServiceClient;
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .build();
//    }
//
//    @Test
//    void contextLoads() throws Exception {
//        mockMvc.perform(get("/api/v1/purchases"))
//                .andExpect(status().isNotFound());
//    }
//}