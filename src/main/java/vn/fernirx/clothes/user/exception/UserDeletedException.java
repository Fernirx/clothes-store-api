package vn.fernirx.clothes.user.exception;

import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.AppException;

public class UserDeletedException extends AppException {
    public UserDeletedException() {
        super(ErrorCode.ACCOUNT_DELETED, "User has been deleted");
    }
}
