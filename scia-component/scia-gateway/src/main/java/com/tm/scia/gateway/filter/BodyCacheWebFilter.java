package com.tm.scia.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class BodyCacheWebFilter implements WebFilter, Ordered {
    private final static Logger LOGGER = LoggerFactory.getLogger(BodyCacheWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        LOGGER.info("Try to cache request body if present!");
        return ServerWebExchangeUtils
                .cacheRequestBody(exchange,
                        (serverHttpRequest) -> chain.filter(
                                exchange.mutate().request(serverHttpRequest).build()))
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
