package com.tm.scia.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class DemoRouteLocatorConfiguration {
    @Bean
    public RouteLocator demoRoute(RouteLocatorBuilder builder) {
        return builder.routes().route(p -> p.method(HttpMethod.GET)
                        .and()
                        .path("/sn", "/sendNotify")
                        .filters(f ->
                                        f.addRequestHeader("Hello", "World!")
//                                .changeRequestUri(exchange -> {
//                                    String uri = exchange.getRequest().getURI().toString();
//                                    URI newUri = URI.create(uri.replace("http://localhost:8081", "http://localhost:8080"));
//                                    return Optional.of(newUri);
//                                })
                        )
                        .uri("http://localhost:8080")
        ).build();
    }

    @Bean
    RouteLocator requestHeader(RouteLocatorBuilder builder) {
        return builder.routes().route(
                predicateSpec -> predicateSpec.path("/demo/header")
                        .filters(filterSpec -> filterSpec.addRequestHeader("userCode", "Toman"))
                        .uri("http://localhost:8080").order(-1)
        ).build();
    }
}
