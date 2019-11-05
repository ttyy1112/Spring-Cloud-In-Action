package com.tm.scia.webflux.rest;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ExceptionRequestController {

    @RequestMapping("/exception")
    public Mono<String> helloWorld(ServerHttpRequest request) {

        return Mono.error(new NullPointerException());
    }
}
