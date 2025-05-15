package com.champsoft.services.sales;

import com.champsoft.services.sales.Client.PaymentsServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class SalesApplicationAndConfigTest {

    @Test
    void objectMapper_isConfiguredProperly() {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper mapper = config.objectMapper();

        assertNotNull(mapper);

        // Check module registration by looking for 'jsr310' in module id string (case-insensitive)
        assertTrue(mapper.getRegisteredModuleIds().stream()
                .map(Object::toString)
                .anyMatch(id -> id.toLowerCase().contains("jsr310")));

        // Check that WRITE_DATES_AS_TIMESTAMPS is disabled
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }



    @Test
    void restTemplateBean_isNotNull() {
        SalesApplication app = new SalesApplication();
        RestTemplate restTemplate = app.restTemplate();
        assertNotNull(restTemplate);
    }

    @TestConfiguration
    public class TestConfig {
        @Bean
        public PaymentsServiceClient paymentsServiceClient() {
            return Mockito.mock(PaymentsServiceClient.class);
        }
    }

    @SpringBootTest
    @Import(TestConfig.class)
    public class SalesApplicationTests {
        // Your test here
    }

}
