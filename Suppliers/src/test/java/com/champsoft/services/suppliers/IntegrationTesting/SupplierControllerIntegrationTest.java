package com.champsoft.services.suppliers.IntegrationTesting;

import com.champsoft.services.suppliers.DataLayer.PhoneType;
import com.champsoft.services.suppliers.DataLayer.SupplierPhoneNumber;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 🧪 Ensures we use application-test.yml
public class SupplierControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        // Optional: Clean the test DB after each test if needed
        // supplierRepository.deleteAll();
    }

    @Test
    public void testAddSupplier_returnsCreated() throws Exception {
        SupplierRequestModel request = new SupplierRequestModel();
        request.setCompanyName("Petal Paradise");
        request.setContactPerson("Bob Bloom");
        request.setEmailAddress("bob@petalparadise.com");
        request.setStreetAddress("456 Rose Ave");
        request.setPostalCode("M2B 2B2");
        request.setCity("Toronto");
        request.setProvince("Ontario");
        request.setUsername("bob123");
        request.setPassword1("secure123");
        request.setPassword2("secure123");

        SupplierPhoneNumber phone = new SupplierPhoneNumber(PhoneType.WORK, "416-234-5678");
        request.setPhoneNumbers(List.of(phone));

        mockMvc.perform(post("/api/v1/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.supplierId", not(emptyOrNullString())))
                .andExpect(jsonPath("$.companyName").value("Petal Paradise"))
                .andExpect(jsonPath("$.phoneNumbers", hasSize(1)));
    }

    @Test
    public void testGetSuppliers_returnsList() throws Exception {
        mockMvc.perform(get("/api/v1/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.supplierResponseModelList", not(empty())));
    }

    @Test
    public void testDeleteNonExistingSupplier_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/suppliers/sup-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
}
