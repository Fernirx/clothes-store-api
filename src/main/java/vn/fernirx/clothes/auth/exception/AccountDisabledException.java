package vn.fernirx.clothes.auth.exception;

import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.SecurityBaseException;

public class AccountDisabledException extends SecurityBaseException {
    public AccountDisabledException() {
        super(ErrorCode.ACCOUNT_DISABLED, "Account is disabled");
    }
}
