package com.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author zdh
 */
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class JsonExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            //对于已经committed(提交)的response，就不能再使用这个response向缓冲区写任何东西
            return Mono.error(ex);
        }
        // header set 响应JSON类型数据，统一响应数据结构（适用于前后端分离JSON数据交换系统）
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 按照异常类型进行翻译处理，翻译的结果易于前端理解
        String message = errAnalyze(ex, response);

        //全局通用响应数据结构，可以自定义。通常包含请求结果code、message、data
        Result result = new Result(response.getStatusCode().value(),message,message,null);

        //记录错误日志
        writeLog(exchange, ex);

        byte[] res = null;
        try {
            res=objectMapper.writeValueAsBytes(result);
        } catch (JsonProcessingException e) {
             writeLog(exchange,e);
            return Mono.error(ex);
        }

        byte[] finalRes = res;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(finalRes);
        }));

    }

    private String errAnalyze(Throwable ex, ServerHttpResponse response) {
        String message;
        if (ex instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            message = "您请求的服务不存在";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            response.setStatusCode(responseStatusException.getStatus());
            message = responseStatusException.getMessage();
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            message = "服务器内部错误";
        }
        return message;
    }


    /**
     *  将错误信息以日志的形式记录下来
     */
    private void writeLog(ServerWebExchange exchange, Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String host = uri.getHost();
        int port = uri.getPort();
        log.error("[gateway]-host:{} ,port:{}，url:{},  errormessage:",
                host,
                port,
                request.getPath(),
                ex);
    }
}