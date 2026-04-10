package vn.fernirx.clothes.auth.service;

import vn.fernirx.clothes.auth.dto.request.*;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);

    TokenResponse refreshToken(RefreshTokenRequest request);

    void register(RegisterRequest request);

    TokenResponse verifyOtp(VerifyOtpRequest request);

    void resendOtp(ResendOtpRequest request);
}
