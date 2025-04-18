package com.champsoft.services.suppliers.DataLoader;

import com.champsoft.services.suppliers.DataLayer.SupplierRepository;
import com.champsoft.services.suppliers.SupplierDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.*;

public class SupplierDataLoaderTest {

    @Test
    void testLoadSuppliers_fromJson() throws Exception {
        SupplierRepository mockRepo = mock(SupplierRepository.class);
        SupplierDataLoader loader = new SupplierDataLoader();

        // Inject test JSON
        String json = """
                [
                  {
                    "supplierIdentifier": "sup-999",
                    "companyName": "TestCo",
                    "contactPerson": "John Doe",
                    "emailAddress": "john@test.com",
                    "username": "johnd",
                    "password": "pw",
                    "address": {
                      "streetAddress": "123 Test",
                      "postalCode": "H1H 1H1",
                      "city": "TestCity",
                      "province": "QC"
                    },
                    "phoneNumbers": []
                  }
                ]
                """;

        loader.setOverrideStream(new ByteArrayInputStream(json.getBytes()));

        CommandLineRunner runner = loader.loadSuppliers(mockRepo);
        runner.run();

        verify(mockRepo, times(1)).saveAll(anyList());
    }

    @Test
    void testLoadSuppliers_fileNotFound() throws Exception {
        SupplierRepository mockRepo = mock(SupplierRepository.class);
        SupplierDataLoader loader = new SupplierDataLoader();

        // Simulate empty or missing file
        loader.setOverrideStream(new ByteArrayInputStream(new byte[0]));

        CommandLineRunner runner = loader.loadSuppliers(mockRepo);
        runner.run();

        verify(mockRepo, never()).saveAll(any());
    }
}
