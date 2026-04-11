package vn.fernirx.clothes.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fernirx.clothes.auth.dto.request.ForgotPasswordRequest;
import vn.fernirx.clothes.auth.dto.request.ResendOtpRequest;
import vn.fernirx.clothes.auth.dto.request.ResetPasswordRequest;
import vn.fernirx.clothes.auth.dto.request.VerifyOtpRequest;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.service.PasswordService;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<SuccessResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        passwordService.forgotPassword(request);
        return ResponseEntity.ok(
                SuccessResponse.of("OTP sent to your email")
        );
    }

    @PostMapping("/forgot/verify")
    public ResponseEntity<SuccessResponse<TokenResponse>> verifyForgotPassword(
            @Valid @RequestBody VerifyOtpRequest request) {
        TokenResponse tokenResponse = passwordService.verifyForgotPassword(request);
        return ResponseEntity.ok(SuccessResponse.of(
                "OTP verified successfully",
                tokenResponse
        ));
    }

    @PostMapping("/forgot/resend")
    public ResponseEntity<SuccessResponse<Void>> resendForgotPassword(
            @Valid @RequestBody ResendOtpRequest request) {
        passwordService.resendOtp(request);
        return ResponseEntity.ok(
                SuccessResponse.of("OTP sent to your email")
        );
    }

    @PostMapping("/forgot/reset")
    public ResponseEntity<SuccessResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok(
                SuccessResponse.of("Password reset successfully")
        );
    }
}
