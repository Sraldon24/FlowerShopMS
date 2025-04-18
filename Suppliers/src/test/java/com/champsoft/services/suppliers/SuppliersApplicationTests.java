package com.champsoft.services.suppliers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test") // ✅ This tells Spring Boot to load application-test.yml
@SpringBootTest
class SuppliersApplicationTests {

    @Test
    void contextLoads() {
    }
}
