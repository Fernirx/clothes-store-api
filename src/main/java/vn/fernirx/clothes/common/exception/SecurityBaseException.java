package vn.fernirx.clothes.common.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class SecurityBaseException extends RuntimeException {
    private final ErrorCode code;

    public SecurityBaseException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
