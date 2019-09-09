package com.tm.scia.webflux.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class DemoRoutesConfiguration {
    @Bean
    public RouterFunction<ServerResponse> demoRoutes() {
        return RouterFunctions.route(
                GET("/route/helloWorld").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().syncBody("Router Hello World!")
        );
    }


}
