package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApiGatewayServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayServiceApplication.class, args);
        System.out.println("Active Profile(s): " +
                String.join(", ", context.getEnvironment().getActiveProfiles()));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
