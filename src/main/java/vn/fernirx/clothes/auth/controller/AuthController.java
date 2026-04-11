package vn.fernirx.clothes.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fernirx.clothes.common.constant.SecurityConstants;
import vn.fernirx.clothes.auth.dto.request.*;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.service.AuthService;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Các API xác thực người dùng")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Đăng nhập",
            description = "Xác thực người dùng bằng email và mật khẩu, trả về access token và refresh token"
    )
    public ResponseEntity<SuccessResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse data = authService.login(loginRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "Login success",
                data
        ));
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Làm mới token",
            description = "Cấp mới access token và refresh token từ refresh token hợp lệ"
    )
    public ResponseEntity<SuccessResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse data = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "Refresh token success",
                data
        ));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Đăng ký tài khoản",
            description = "Tạo tài khoản mới và gửi OTP xác thực đến email"
    )
    public ResponseEntity<SuccessResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.accepted()
                .body(SuccessResponse.of("Register success"));
    }

    @PostMapping("/verify-otp")
    @Operation(
            summary = "Xác thực OTP đăng ký",
            description = "Xác thực mã OTP sau khi đăng ký, trả về access token và refresh token nếu thành công"
    )
    public ResponseEntity<SuccessResponse<TokenResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        TokenResponse data = authService.verifyOtp(request);
        return ResponseEntity.ok(SuccessResponse.of(
                        "Verify success",
                        data
                ));
    }

    @PostMapping("/resend-otp")
    @Operation(
            summary = "Gửi lại OTP đăng ký",
            description = "Gửi lại mã OTP xác thực đến email khi đăng ký"
    )
    public ResponseEntity<SuccessResponse<Void>> resendOtp(
            @Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok(
                SuccessResponse.of("Resend success")
        );
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Đăng xuất",
            description = "Vô hiệu hóa access token và refresh token hiện tại"
    )
    public ResponseEntity<SuccessResponse<Void>> logout(
            HttpServletRequest request,
            @Valid @RequestBody LogoutRequest logoutRequest) {
        String header = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            authService.logout(
                    header.substring(SecurityConstants.BEARER_PREFIX_LENGTH),
                    logoutRequest.refreshToken()
            );
        }
        return ResponseEntity.ok(SuccessResponse.of("Logout success"));
    }
}
