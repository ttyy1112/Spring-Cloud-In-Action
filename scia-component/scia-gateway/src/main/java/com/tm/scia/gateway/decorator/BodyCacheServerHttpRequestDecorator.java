package com.tm.scia.gateway.decorator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


public class BodyCacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {
    private Logger LOGGER = LoggerFactory.getLogger(BodyCacheServerHttpRequestDecorator.class);

    private boolean firstSubscribe = true;
    private List<byte[]> listByteArray = new ArrayList<>();

    public BodyCacheServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        if (firstSubscribe) {
//            return super.getBody().map(dataBuffer -> ((NettyDataBuffer) dataBuffer)
//                    .getNativeBuffer()).map(byteBuf -> {
//                firstSubscribe = false;
//
//                byte[] dst = new byte[byteBuf.readableBytes()];
//                byteBuf.readBytes(dst);
//                listByteArray.add(dst);
//
//                byteBuf.release();
//
//                return getDataBuffer(dst);
//            });

            return super.getBody().map(dataBuffer -> {
                firstSubscribe = false;

                ByteBuf copy = ((NettyDataBuffer) dataBuffer).getNativeBuffer().copy();
                byte[] dst = new byte[copy.readableBytes()];
                copy.readBytes(dst);
                listByteArray.add(dst);

                copy.release();

                return dataBuffer;
            });
        } else {
            return Flux.fromIterable(listByteArray).map(bytes -> getDataBuffer(bytes));
        }
    }

    private DataBuffer getDataBuffer(byte[] bytes) {
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        return nettyDataBufferFactory.wrap(bytes);
    }
}
