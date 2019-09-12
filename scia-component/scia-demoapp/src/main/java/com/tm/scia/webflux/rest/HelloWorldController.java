package com.tm.scia.webflux.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class HelloWorldController {
    @Autowired
    private DemoProperties demoProperties;


    @RequestMapping("/helloWorld")
    public Mono<String> helloWorld() {
        return Mono.just("Hello World!" + demoProperties.getArgs().get("list.0"));
    }
}
