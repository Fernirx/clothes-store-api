package vn.fernirx.clothes.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class TokenException extends AuthenticationException {
    private final ErrorCode code;

    public TokenException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public static TokenException invalid() {
        return new TokenException(ErrorCode.TOKEN_INVALID, "Invalid authentication token");
    }

    public static TokenException expired() {
        return new TokenException(ErrorCode.TOKEN_EXPIRED, "Authentication token has expired");
    }
}
