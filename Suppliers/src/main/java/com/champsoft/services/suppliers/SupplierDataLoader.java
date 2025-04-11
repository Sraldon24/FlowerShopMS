package com.champsoft.services.suppliers;

import com.champsoft.services.suppliers.DataLayer.Supplier;
import com.champsoft.services.suppliers.DataLayer.SupplierRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class SupplierDataLoader {

    @Bean
    CommandLineRunner loadSuppliers(SupplierRepository supplierRepository) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Supplier>> typeRef = new TypeReference<>() {};

            InputStream inputStream = getClass()
                    .getResourceAsStream("/suppliers.json");

            if (inputStream != null) {
                List<Supplier> suppliers = mapper.readValue(inputStream, typeRef);
                suppliers.forEach(s -> {
                    if (s.getSupplierIdentifier() == null || s.getSupplierIdentifier().isEmpty()) {
                        s.setSupplierIdentifier(java.util.UUID.randomUUID().toString());
                    }
                });
                supplierRepository.saveAll(suppliers);
                System.out.println("✅ Loaded " + suppliers.size() + " suppliers from JSON.");
            } else {
                System.out.println("⚠️ suppliers.json not found!");
            }
        };
    }
}
