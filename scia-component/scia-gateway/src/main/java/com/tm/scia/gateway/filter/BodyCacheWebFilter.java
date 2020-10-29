package com.tm.scia.gateway.filter;

import com.tm.scia.gateway.decorator.BodyCacheServerWebExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return chain.filter(new BodyCacheServerWebExchange(exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
