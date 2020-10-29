package com.tm.scia.gateway.filter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class RequestLoggingWebFilter implements WebFilter, Ordered {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestLoggingWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Receive request: " + exchange.getRequest().getURI().toString());
        }

        return chain.filter(exchange).doOnSuccessOrError((t, thr) -> {
            DataBufferUtils.join(exchange.getRequest().getBody()).
                    subscribe(
                            dataBuffer -> {
                                try {
                                    String body = IOUtils.toString(dataBuffer.asInputStream());
                                    LOGGER.info(body);
                                } catch (IOException e) {
                                    LOGGER.error("Write input stream to String exception.", e);
                                } finally {
                                    ((NettyDataBuffer) dataBuffer).release();
                                }
                            }
                    );
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
