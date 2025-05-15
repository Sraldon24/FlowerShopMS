package com.champsoft.services.payment.IntegrationTesting;

import com.champsoft.services.payment.DataLayer.PaymentRepository;
import com.champsoft.services.payment.PresentationLayer.PaymentRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        paymentRepository.deleteAll();
    }

    @Test
    public void testAddPayment_andGetById() throws Exception {
        PaymentRequestModel request = new PaymentRequestModel(
                99.99,
                "PAYPAL",
                "PENDING",
                ZonedDateTime.parse("2025-05-14T12:00:00Z"),
                "TXN-001-MOCK"
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        // POST
        String jsonResponse = mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", is(99.99)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String paymentId = objectMapper.readTree(jsonResponse).get("paymentIdentifier").asText();

        // GET by ID
        mockMvc.perform(get("/api/v1/payments/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentIdentifier", is(paymentId)))
                .andExpect(jsonPath("$.method", is("PAYPAL")));
    }

    @Test
    public void testDeletePayment() throws Exception {
        // Add payment first
        PaymentRequestModel request = new PaymentRequestModel(
                55.0, "CASH", "COMPLETED", ZonedDateTime.now(), "TXN-DELETE"
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        String jsonResponse = mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String paymentId = objectMapper.readTree(jsonResponse).get("paymentIdentifier").asText();

        // Delete
        mockMvc.perform(delete("/api/v1/payments/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment with ID " + paymentId + " deleted successfully."));
    }
}
