package com.tm.scia.webflux.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequestMapping("/game/animal")
@RestController
public class AnimalController {
    private Logger LOGGER = LoggerFactory.getLogger(AnimalController.class);

    private final List<String> ANIMALS = Arrays.asList("Monkey", "Tiger", "Elephant", "kangaroo", "Rabbit", "Goat", "Mouse", "Cat", "Dog", "Chicken", "Ant", "Bear");

    @RequestMapping("/get")
    public Mono<String> getAnimal(ServerHttpRequest request, @RequestBody(required = false) String body) {
        LOGGER.info("com.jd.chainet.demo.rest.AnimalController.getAnimal start");

        List<String> fromList = request.getHeaders().get("from");

        if (CollectionUtils.isEmpty(fromList)) {
            return Mono.just("From is required in header!");
        }

        StringBuilder sb = new StringBuilder()
                .append("Request from ")
                .append(CollectionUtils.isEmpty(fromList) ? "Universal" : fromList.get(0))
                .append("! ").append("Congratulations! You got a pet ")
                .append(ANIMALS.get(new Random().nextInt(ANIMALS.size() - 1)))
                .append("!");

        LOGGER.info("com.jd.chainet.demo.rest.AnimalController.getAnimal end");
        return Mono.just(
                sb.append("\n").append(
                        Optional.ofNullable(body).map(b -> "Request Body = " + b).orElse("")
                ).toString()
        );
    }
}
