package vn.fernirx.clothes.auth.service;

import vn.fernirx.clothes.auth.dto.request.ForgotPasswordRequest;
import vn.fernirx.clothes.auth.dto.request.ResendOtpRequest;
import vn.fernirx.clothes.auth.dto.request.ResetPasswordRequest;
import vn.fernirx.clothes.auth.dto.request.VerifyOtpRequest;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;

public interface PasswordService {
    void forgotPassword(ForgotPasswordRequest request);

    TokenResponse verifyForgotPassword(VerifyOtpRequest request);

    void resendOtp(ResendOtpRequest request);

    void resetPassword(ResetPasswordRequest request);
}
