package com.solution.apigateway.configuration;

import com.solution.apigateway.filter.LoggingFilter;
import com.solution.apigateway.filter.SecurityFilters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes(SecurityFilters sf, LoggingFilter lf) {
        return route()
                .add(route()
                        .route(path("/api/auth/**"), http())
                        .filter(lb("auth-service"))
                        .filter(lf.logging())
                        .build())
                .add(route()
                        .route(path("/api/ingestion/**"), http())
                        .filter(lb("log-ingestion-service"))
                        .filter(lf.logging())
                        .filter(sf.apiKey())
                        .build())
                .add(route()
                        .route(path("/api/core/**"), http())
                        .filter(lb("core-service"))
                        .filter(lf.logging())
                        .filter(sf.jwtAuth())
                        .build())
                .add(route()
                        .route(path("/api/notification/**"), http())
                        .filter(lb("notification-service"))
                        .filter(lf.logging())
                        .filter(sf.jwtAuth())
                        .build())
                .build();
    }
}
