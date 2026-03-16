package vn.fernirx.clothes.common.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class ResourceInUseException extends AppException {
    public ResourceInUseException(String resourceName) {
        super(ErrorCode.IN_USE, String.format("%s is currently in use", resourceName));
    }
}
