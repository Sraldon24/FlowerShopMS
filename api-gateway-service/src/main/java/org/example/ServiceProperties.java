package org.example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class ServiceProperties {
    private Service suppliersService;
    private Service employeeService;
    private Service inventoryService;
    private Service salesService;

    @Getter
    @Setter
    public static class Service {
        private String host;
        private int port;
    }
}