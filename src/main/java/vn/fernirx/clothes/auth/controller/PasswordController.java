package vn.fernirx.clothes.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Password API", description = "Các API quản lý mật khẩu")
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/forgot")
    @Operation(
            summary = "Quên mật khẩu",
            description = "Gửi mã OTP xác thực đến email để bắt đầu quá trình đặt lại mật khẩu"
    )
    public ResponseEntity<SuccessResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        passwordService.forgotPassword(request);
        return ResponseEntity.ok(
                SuccessResponse.of("OTP sent to your email")
        );
    }

    @PostMapping("/forgot/verify")
    @Operation(
            summary = "Xác thực OTP quên mật khẩu",
            description = "Xác thực mã OTP để lấy reset token dùng cho bước đặt lại mật khẩu"
    )
    public ResponseEntity<SuccessResponse<TokenResponse>> verifyForgotPassword(
            @Valid @RequestBody VerifyOtpRequest request) {
        TokenResponse tokenResponse = passwordService.verifyForgotPassword(request);
        return ResponseEntity.ok(SuccessResponse.of(
                "OTP verified successfully",
                tokenResponse
        ));
    }

    @PostMapping("/forgot/resend")
    @Operation(
            summary = "Gửi lại OTP quên mật khẩu",
            description = "Gửi lại mã OTP xác thực đến email khi quên mật khẩu"
    )
    public ResponseEntity<SuccessResponse<Void>> resendForgotPassword(
            @Valid @RequestBody ResendOtpRequest request) {
        passwordService.resendOtp(request);
        return ResponseEntity.ok(
                SuccessResponse.of("OTP sent to your email")
        );
    }

    @PostMapping("/forgot/reset")
    @Operation(
            summary = "Đặt lại mật khẩu",
            description = "Đặt mật khẩu mới bằng reset token đã được xác thực qua OTP"
    )
    public ResponseEntity<SuccessResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok(
                SuccessResponse.of("Password reset successfully")
        );
    }
}
