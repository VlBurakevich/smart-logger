package com.solution.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class AccountValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<AccountValidationGatewayFilterFactory.Config> {
    private final ReactiveStringRedisTemplate redisTemplate;

    public AccountValidationGatewayFilterFactory(ReactiveStringRedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    public static class Config {
        //maybe
    }

    @Override
    public GatewayFilter apply(AccountValidationGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            String accountId = exchange.getRequest().getQueryParams().getFirst("accountId");

            if (accountId == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing account id parameter"));
            }

            return redisTemplate.hasKey("accounts:" + accountId)
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists)) {
                            return chain.filter(exchange);
                        } else {
                            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is inactive or invalid")); //TODO check in core-service
                        }
                    });
        };
    }
}
