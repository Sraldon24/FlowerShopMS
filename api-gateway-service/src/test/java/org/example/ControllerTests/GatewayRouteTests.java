//package org.example.ControllerTests;
//
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import reactor.core.publisher.Flux;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class GatewayRouteTests {
//
//    @Autowired
//    private RouteDefinitionLocator routeDefinitionLocator;
//
//    @Test
//    void shouldLoadAtLeastOneRouteDefinition() {
//        Flux<RouteDefinition> routes = routeDefinitionLocator.getRouteDefinitions();
//
//        List<RouteDefinition> routeList = routes.collectList().block(); // Block to get the list synchronously
//
//        assertThat(routeList).isNotNull();
//        assertThat(routeList).isNotEmpty();
//        System.out.println("Number of routes loaded: " + (routeList != null ? routeList.size() : 0));
//        if (routeList != null) {
//            routeList.forEach(route -> System.out.println("Route ID: " + route.getId()));
//        }
//    }
//}