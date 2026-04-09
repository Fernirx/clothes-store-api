package vn.fernirx.clothes.auth.exception;

import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.AppException;

public class OTPException extends AppException {
    public OTPException(ErrorCode code, String message) {
        super(code, message);
    }

    public static OTPException validationFailed() {
        return new OTPException(ErrorCode.OTP_VALIDATION_FAILED, "OTP validation failed");
    }

    public static OTPException maxAttemptsExceeded() {
        return new OTPException(ErrorCode.OTP_MAX_ATTEMPTS_EXCEED, "Maximum OTP attempts exceeded");
    }

    public static OTPException maxResendExceeded() {
        return new OTPException(ErrorCode.OTP_MAX_RESEND_EXCEEDED, "Maximum OTP resend attempts exceeded");
    }

    public static OTPException resendCooldownExceeded() {
        return new OTPException(ErrorCode.OTP_RESEND_COOLDOWN, "You must wait before requesting another OTP");
    }
}
