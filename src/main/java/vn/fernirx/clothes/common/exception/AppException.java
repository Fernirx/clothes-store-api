package vn.fernirx.clothes.common.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode code;

    public AppException(ErrorCode code) {
        this.code = code;
    }

    public AppException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
