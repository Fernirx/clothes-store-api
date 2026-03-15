package vn.fernirx.clothes.common.constant;

import java.util.List;

public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }

    /* ================== PUBLIC ENDPOINTS ================== */
    public static final String[] PUBLIC_ENDPOINTS = {
            "/swagger-docs/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/actuator/health"
    };

    /* ================== SKIP PATHS ================== */
    public static final List<String> SKIP_PATHS = List.of(
            "/v3/api-docs",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/actuator/health",
            "/actuator/info"
    );

    /* ================== JWT ================== */
    public static final String JWT_ACCESS_TOKEN = "access_token";
    public static final String JWT_REFRESH_TOKEN = "refresh_token";
    public static final String JWT_RESET_PASSWORD_TOKEN = "reset_password_token";
    public static final String JWT_CLAIMS_TYPE = "type";
    public static final String JWT_CLAIMS_EMAIL = "email";
    public static final String JWT_CLAIMS_AUTHORITIES = "authorities";

    /* ================== HTTP HEADERS ================== */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";
    public static final int BEARER_PREFIX_LENGTH = 7;
    public static final String CONTENT_TYPE_JSON = "application/json";
}
