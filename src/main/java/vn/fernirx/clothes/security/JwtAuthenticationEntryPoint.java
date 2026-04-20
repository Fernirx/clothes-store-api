package vn.fernirx.clothes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.common.constant.SecurityConstants;
import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.response.ErrorResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         HttpServletResponse response,
                         @NonNull AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(SecurityConstants.CONTENT_TYPE_JSON);
        ErrorResponse errorResponse;
        int statusCode;
        statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        errorResponse = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED, authException.getMessage());
        response.setStatus(statusCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}
