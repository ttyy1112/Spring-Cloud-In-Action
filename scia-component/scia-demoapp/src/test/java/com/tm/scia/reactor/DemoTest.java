package com.tm.scia.reactor;

import org.junit.Test;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DemoTest {
    @Test
    public void testFlux() {
        Flux.just("tom", "jack", "allen")
                .map(s -> s.concat("@qq.com"))
                .filter(s -> s.startsWith("tom"))
                .subscribe(System.out::println);
    }

    @Test
    public void testExceptionHandler() {
        Flux.just("tom", "jack", "elephant")
                .map(t -> t.substring(3, 6))
                .onErrorContinue((ex, obj) -> System.out.println(ex))
                .subscribe(t -> System.out.println(t));
    }

    @Test
    public void testFluxMap() {
        Flux<String> map = Flux.just("tom", "jack", "elephant")
                .map(t -> t.substring(1));

        map.subscribe(t -> System.out.println(t));
        map.subscribe(t -> System.out.println("an: " + t));
    }

    @Test
    public void testFluxDefer() {
        Flux<String> fluxDefer = Flux.defer(() -> Flux.just("A", "B", "C"));

        fluxDefer.subscribe(t -> System.out.println(t));
        fluxDefer.subscribe(t -> System.out.println("an: " + t));
    }

    @Test
    public void testFluxWithoutSubscribe() {
        Flux.just("tom", "jack", "allen")
                .map(s -> s.concat("@qq.com"))
                .filter(s -> s.startsWith("tom"));
    }
}
