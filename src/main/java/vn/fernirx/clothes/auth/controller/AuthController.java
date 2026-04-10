package vn.fernirx.clothes.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fernirx.clothes.auth.dto.request.*;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.service.AuthService;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse data = authService.login(loginRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "Login success",
                data
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse data = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "Refresh token success",
                data
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.accepted()
                .body(SuccessResponse.of("Register success"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<SuccessResponse<TokenResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        TokenResponse data = authService.verifyOtp(request);
        return ResponseEntity.ok(SuccessResponse.of(
                        "Verify success",
                        data
                ));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<SuccessResponse<Void>> resendOtp(
            @Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok(
                SuccessResponse.of("Resend success")
        );
    }
}
