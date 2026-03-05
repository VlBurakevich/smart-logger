package com.solution.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Slf4j
@Component
public class LoggingFilter {

    public HandlerFilterFunction<ServerResponse, ServerResponse> logging() {
        return (request, next) -> {
            long startTime = System.currentTimeMillis();

            log.info("==> Incoming: {} {}", request.method(), request.path());

            try {
                ServerResponse response = next.handle(request);
                long duration = System.currentTimeMillis() - startTime;

                log.info("<== Outgoing: {} {}, Status: {}, Time: {}ms",
                        request.method(), request.path(), response.statusCode().value(), duration);

                return response;
            } catch (Exception e) {
                log.error("<== Failed: {} {}, Error: {}ms", request.method(), request.path(), e.getMessage());
                throw e;
            }
        };
    }
}
