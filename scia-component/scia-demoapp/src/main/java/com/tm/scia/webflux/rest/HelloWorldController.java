package com.tm.scia.webflux.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloWorldController {

    @RequestMapping("/helloWorld")
    public Mono<String> helloWorld() {
        return Mono.just("Hello World!");
    }
}
