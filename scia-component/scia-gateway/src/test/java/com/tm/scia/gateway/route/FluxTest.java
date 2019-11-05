package com.tm.scia.gateway.route;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class FluxTest {
    @Test
    public void testCacheFlux() throws InterruptedException {
        AtomicReference<Context> storeRef = new AtomicReference<>(Context.empty());

        Flux<Integer> cachedFlux = CacheFlux
                .lookup(k -> Mono.justOrEmpty(storeRef.get().getOrEmpty(k))
                                .cast(Integer.class)
                                .flatMap(max -> Flux.range(1, max)
                                        .materialize()
                                        .collectList()),
                        "myCache")
                .onCacheMissResume(() -> {
                    System.out.println(Thread.currentThread().getName() + " : Cache missing!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Flux.range(1, 10);
                })
                .andWriteWith((k, sigs) -> Flux.fromIterable(sigs)
                        .dematerialize()
                        .last()
                        .doOnNext((max) -> {
                            System.out.println(Thread.currentThread().getName() + " : " + String.format("put key [%s] value [%s]", k, max));
                            storeRef.updateAndGet(ctx -> ctx.put(k, max));
                        })
                        .then());

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executorService.submit(() -> cachedFlux.subscribe(t -> System.out.println(Thread.currentThread().getName() + " : " + t)));
        }

        Thread.sleep(10000l);
    }

    @Test
    public void testCacheFlux2() throws InterruptedException {
        LoadingCache<String, Object> graphs = Caffeine
                .newBuilder()
                .maximumSize(1)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(key -> createExpensiveGraph(key));

        CacheFlux
                .lookup(graphs.asMap(), "one", Integer.class)
                .onCacheMissResume(findAllByCategory("one")).subscribe((t) -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t);
        });

        CacheFlux
                .lookup(graphs.asMap(), "two", Integer.class)
                .onCacheMissResume(findAllByCategory("two")).subscribe((t) -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t);
        });
        CacheFlux
                .lookup(graphs.asMap(), "two", Integer.class)
                .onCacheMissResume(findAllByCategory("two")).subscribe((t) -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t);
        });
        CacheFlux
                .lookup(graphs.asMap(), "one", Integer.class)
                .onCacheMissResume(findAllByCategory("one")).subscribe((t) -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t);
        });


        Thread.sleep(60_000);
    }

    private Flux<Integer> findAllByCategory(String key) {
        System.out.println("com.tm.scia.gateway.route.FluxTest.findAllByCategory");
        return Flux.range(1, 10);
    }

    private Object createExpensiveGraph(String key) {
        System.out.println("com.tm.scia.gateway.route.FluxTest.createExpensiveGraph");
        return Flux.range(5, 5);
    }
}
