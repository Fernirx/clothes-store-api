package vn.fernirx.clothes.common.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class ResourceNotFoundException extends AppException {
    public ResourceNotFoundException(String resourceName) {
        super(ErrorCode.NOT_FOUND, String.format("%s not found", resourceName));
    }
}
