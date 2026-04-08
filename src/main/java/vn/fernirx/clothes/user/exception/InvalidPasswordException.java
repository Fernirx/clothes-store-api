package vn.fernirx.clothes.user.exception;

import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.AppException;

public class InvalidPasswordException extends AppException {
    public InvalidPasswordException(ErrorCode code, String message)  {
        super(code, message);
    }

    public static InvalidPasswordException oldPasswordIncorrect() {
        return new InvalidPasswordException(
                ErrorCode.INVALID_PASSWORD,
                "Old password is incorrect"
        );
    }

    public static InvalidPasswordException reuseNotAllowed() {
        return new InvalidPasswordException(
                ErrorCode.INVALID_PASSWORD,
                "New password must be different from old password"
        );
    }
}
