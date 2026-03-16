package vn.fernirx.clothes.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;

@Getter
public class ResourceAlreadyExistsException extends AppException{
    public ResourceAlreadyExistsException(String resourceName) {
        super(ErrorCode.ALREADY_EXISTS, String.format("%s already exists", resourceName));
    }
}
