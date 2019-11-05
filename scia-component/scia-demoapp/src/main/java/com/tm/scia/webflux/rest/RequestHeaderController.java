package com.tm.scia.webflux.rest;

import net.minidev.json.JSONArray;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class RequestHeaderController {

    @RequestMapping("/demo/header")
    public Mono<List<String>> helloWorld(ServerHttpRequest request) {

        return Flux.fromIterable(request.getHeaders().entrySet())
                .map(entry -> entry.getKey() + ":" + JSONArray.toJSONString(entry.getValue()))
                .collectList();
    }
}
