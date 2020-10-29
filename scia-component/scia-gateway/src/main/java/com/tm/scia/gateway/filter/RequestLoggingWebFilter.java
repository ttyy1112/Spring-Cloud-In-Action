package com.tm.scia.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Component
public class RequestLoggingWebFilter implements WebFilter, Ordered {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestLoggingWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Receive request: " + exchange.getRequest().getURI().toString());
        }

        //LOGGER.info("======" + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());

        // 设置请求时间
        exchange.getAttributes().put("LOGGING_REQUEST_TIME", ZonedDateTime.now());
        Instant requestInstant = Instant.now();

        return chain.filter(exchange).doOnSuccessOrError((t, thr) -> {
            HashMap<String, String> statitic = new HashMap<>();
            // 请求时间
            Optional<Object> loggingRequestTime = Optional.ofNullable(exchange.getAttribute("LOGGING_REQUEST_TIME"));
            if (loggingRequestTime.isPresent()) {
                ZonedDateTime requestTime = (ZonedDateTime) loggingRequestTime.get();
                statitic.put("LOGGING_REQUEST_TIME", requestTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
            } else {
                statitic.put("LOGGING_REQUEST_TIME", "");
            }
            // 请求URI
            statitic.put("LOGGING_REQUEST_URI", exchange.getRequest().getURI().toString());
            // 实际路径
            statitic.put("LOGGING_REQUEST_PATH", exchange.getRequest().getURI().getRawPath());
            // 网关请求URI\Path
            Optional<Object> gatewayRequestURL = Optional.ofNullable(exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR));
            if (gatewayRequestURL.isPresent()) {
                URI uri = (URI) gatewayRequestURL.get();
                statitic.put("LOGGING_GATEWAY_REQUEST_URI", uri.toString());
                statitic.put("LOGGING_GATEWAY_REQUEST_PATH", uri.getRawPath());
            } else {
                statitic.put("LOGGING_GATEWAY_REQUEST_URI", "");
                statitic.put("LOGGING_GATEWAY_REQUEST_PATH", "");
            }
            // 请求Header
            statitic.put("LOGGING_GATEWAY_REQUEST_HEADER", JSONObject.toJSONString(exchange.getRequest().getHeaders()));

            statitic.put("LOGGING_REQUEST_BODY", "");
            DataBufferUtils.join(exchange.getRequest().getBody()).
                    subscribe(
                            dataBuffer -> {
                                try {
                                    String body = IOUtils.toString(dataBuffer.asInputStream());
                                    statitic.put("LOGGING_REQUEST_BODY", body);
                                } catch (IOException e) {
                                    LOGGER.error("Write input stream to String exception.", e);
                                } finally {
                                    ((NettyDataBuffer) dataBuffer).release();
                                }
                            }
                    );

            // 匹配路由
            Optional<Object> gatewayRoute = Optional.ofNullable(exchange.getAttribute(GATEWAY_ROUTE_ATTR));
            if (gatewayRoute.isPresent()) {
                statitic.put("LOGGING_GATEWAY_ROUTE", JSONObject.toJSONString(gatewayRoute.get()));
            } else {
                statitic.put("LOGGING_GATEWAY_ROUTE", "");
            }
            // 响应时间
            statitic.put("LOGGING_RESPONSE_TIME", ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
            // response status
            Optional<HttpStatus> statusCode = Optional.ofNullable(exchange.getResponse().getStatusCode());
            if (statusCode.isPresent()) {
                statitic.put("LOGGING_RESPONSE_STATUS", String.valueOf(statusCode.get().value()));
            } else {
                statitic.put("LOGGING_RESPONSE_STATUS", "");
            }
            // response duration
            Instant responseInstant = Instant.now();
            statitic.put("LOGGING_RESPONSE_DURATION", String.valueOf(Duration.between(requestInstant, responseInstant).toMillis()));
            // exception
            // exception_desc
            // 4xx, 5xx 错误
            Optional<Throwable> exception = Optional.ofNullable(thr);
            if (exception.isPresent() ||
                    !statusCode.isPresent() ||
                    statusCode.get().series() == HttpStatus.Series.CLIENT_ERROR ||
                    statusCode.get().series() == HttpStatus.Series.SERVER_ERROR) {
                statitic.put("LOGGING_EXECUTION_RESULT", "FAILURE");
            } else {
                statitic.put("LOGGING_EXECUTION_RESULT", "SUCCESS");
            }

            if (exception.isPresent()) {
                StringBuilderWriter out = new StringBuilderWriter();
                thr.printStackTrace(new PrintWriter(out));
                statitic.put("LOGGING_EXECUTION_EXCEPTION", out.getBuilder().toString());
            } else {
                statitic.put("LOGGING_EXECUTION_EXCEPTION", "");
            }

            // 写入日志文件
            LOGGER.info(JSONObject.toJSONString(statitic));
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
