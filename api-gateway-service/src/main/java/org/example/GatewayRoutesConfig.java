package org.example;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("suppliers_route", r -> r.path("/api/v1/suppliers/**")
                        .uri("http://suppliers-service:8080"))
                .route("employees_route", r -> r.path("/api/v1/employees/**")
                        .uri("http://employee-service:8080"))
                .route("inventory_route", r -> r.path("/api/v1/inventories/**")
                        .uri("http://inventory-service:8080"))
                .route("flowers_route", r -> r.path("/api/v1/flowers/**")
                        .uri("http://inventory-service:8080"))
                .route("sales_route", r -> r.path("/api/v1/purchases/**")
                        .uri("http://sales-service:8080"))
                .build();
    }
}
