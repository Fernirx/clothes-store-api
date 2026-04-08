package vn.fernirx.clothes.auth.exception;

import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.SecurityBaseException;

public class InvalidCredentialsException extends SecurityBaseException {
    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS, "Invalid credentials");
    }
}