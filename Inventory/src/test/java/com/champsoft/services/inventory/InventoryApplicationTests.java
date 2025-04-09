package com.champsoft.services.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.app.suppliers-service.host=localhost",
        "spring.app.suppliers-service.port=8081"
})
class InventoryApplicationTests {

    @Test
    void contextLoads() {
    }

}
