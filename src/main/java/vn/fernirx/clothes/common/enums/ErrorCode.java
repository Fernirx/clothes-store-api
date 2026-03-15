package vn.fernirx.clothes.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* ================== SERVER ================== */
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE",HttpStatus.SERVICE_UNAVAILABLE),

    /* ================== CLIENT REQUEST ================== */
    BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST),
    TOO_MANY_REQUESTS("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS),
    MALFORMED_JSON("MALFORMED_JSON", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("INVALID_PARAMETER", HttpStatus.BAD_REQUEST),

    /* ================== AUTH / SECURITY ================== */
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("TOKEN_INVALID", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ACCESS_DENIED", HttpStatus.FORBIDDEN),

    /* ================== OTP ================== */
    OTP_VALIDATION_FAILED("OTP_VALIDATION_FAILED", HttpStatus.BAD_REQUEST),
    OTP_MAX_ATTEMPTS_EXCEED("OTP_MAX_ATTEMPTS_EXCEED", HttpStatus.BAD_REQUEST),
    OTP_MAX_RESEND_EXCEEDED("OTP_MAX_RESEND_EXCEEDED", HttpStatus.TOO_MANY_REQUESTS),
    OTP_RESEND_COOLDOWN("OTP_RESEND_COOLDOWN", HttpStatus.TOO_MANY_REQUESTS),

    /* ================== RESOURCE ================== */
    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("ALREADY_EXISTS", HttpStatus.CONFLICT),
    IN_USE("IN_USE", HttpStatus.CONFLICT);

    private final String code;
    private final HttpStatus httpStatus;

    public static Optional<ErrorCode> fromCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst();
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
