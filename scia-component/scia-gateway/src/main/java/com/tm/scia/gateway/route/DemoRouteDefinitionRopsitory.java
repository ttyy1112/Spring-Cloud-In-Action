package com.tm.scia.gateway.route;

import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;

@Component
public class DemoRouteDefinitionRopsitory implements RouteDefinitionRepository {
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {

        RouteDefinition routeDefinition = new RouteDefinition();
        LinkedList<PredicateDefinition> predicates = new LinkedList<>();

        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
        HashMap<String, String> args = new HashMap<>();
        args.put("pattern.0", "/sn");
        args.put("pattern.1", "/sendNotify");
        predicateDefinition.setArgs(args);
        predicates.add(predicateDefinition);

        routeDefinition.setPredicates(predicates);
        routeDefinition.setUri(URI.create("http://localhost:8080"));
        return Flux.just(routeDefinition);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
