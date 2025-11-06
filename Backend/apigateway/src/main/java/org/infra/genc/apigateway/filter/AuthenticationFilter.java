package org.infra.genc.apigateway.filter;

import org.infra.genc.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public static final String AUTH_USER_ID_HEADER = "X-Auth-User-Id";
    public static final String AUTH_USER_ROLES_HEADER = "X-Auth-User-Roles";

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    // Role-based access control map
    private static final Map<String, List<String>> routeRoleMap = Map.of(
            "/api/v1/analytics-service", List.of("ROLE_ADMIN"),
            "/api/v1/cart-service", List.of("ROLE_CUSTOMER", "ROLE_ADMIN"),
            "/api/v1/product-service", List.of("ROLE_CUSTOMER", "ROLE_ADMIN"),
            "/api/v1/order-service", List.of("ROLE_CUSTOMER", "ROLE_ADMIN")
    );

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (validator.isSecured.test(request)) {
                List<String> authValues = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);

                if (authValues.isEmpty()) {
                    log.warn("Missing Authorization header for request {}", request.getURI());
                    return setUnauthorizedResponse(response);
                }

                String authHeader = authValues.get(0);
                if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                    log.warn("Invalid Authorization header format for request {}", request.getURI());
                    return setUnauthorizedResponse(response);
                }

                String token = authHeader.substring(7);
                try {
                    jwtUtil.validateToken(token);
                    Claims claims = jwtUtil.extractAllClaims(token);

                    String userId = claims.getSubject();
                    String userRoles = claims.get("roles", String.class);

                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header(AUTH_USER_ID_HEADER, userId)
                            .header(AUTH_USER_ROLES_HEADER, userRoles != null ? userRoles : "GUEST")
                            .build();

                    exchange = exchange.mutate().request(modifiedRequest).build();

                    // 🔐 Enforce role-based access
                    String path = request.getPath().toString();
                    if (!isAccessAllowed(path, userRoles)) {
                        log.warn("Access denied for user {} with roles {} on path {}", userId, userRoles, path);
                        return setForbiddenResponse(response);
                    }

                } catch (Exception e) {
                    log.warn("Token validation failed for request {}: {}", request.getURI(), e.getMessage());
                    return setUnauthorizedResponse(response);
                }
            }

            log.debug("Authentication/Authorization Context passed for {}", request.getURI());
            log.info("AuthenticationFilter triggered for {}", request.getURI());

            return chain.filter(exchange);
        };
    }

    private boolean isAccessAllowed(String path, String roles) {
        for (Map.Entry<String, List<String>> entry : routeRoleMap.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue().stream().anyMatch(roles::contains);
            }
        }
        return true; // default allow
    }

    private reactor.core.publisher.Mono<Void> setUnauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private reactor.core.publisher.Mono<Void> setForbiddenResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public static class Config {
        // Configuration placeholder
    }
}
