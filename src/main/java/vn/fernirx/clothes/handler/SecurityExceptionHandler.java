package vn.fernirx.clothes.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.fernirx.clothes.auth.exception.InvalidCredentialsException;
import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.SecurityBaseException;
import vn.fernirx.clothes.common.exception.TokenException;
import vn.fernirx.clothes.common.response.ErrorResponse;

@ControllerAdvice
@Order(1)
public class SecurityExceptionHandler {
    @ExceptionHandler(SecurityBaseException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            SecurityBaseException ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ex.getCode();
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                ex.getMessage()
        );
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException ex) {
        ErrorCode errorCode = ex.getCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode, ex.getMessage()));
    }
}
