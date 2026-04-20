package vn.fernirx.clothes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.common.constant.SecurityConstants;
import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.response.ErrorResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull AccessDeniedException accessDeniedException) throws IOException {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        int statusCode = errorCode.getHttpStatus().value();
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                "You do not have permission to access this resource, ba"
        );
        response.setContentType(SecurityConstants.CONTENT_TYPE_JSON);
        response.setStatus(statusCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}