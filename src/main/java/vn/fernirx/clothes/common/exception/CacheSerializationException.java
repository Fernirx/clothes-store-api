package vn.fernirx.clothes.common.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class CacheSerializationException extends AppException {
    private final boolean internal = true;

    public CacheSerializationException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message, cause);
    }
}
