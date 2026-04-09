package vn.fernirx.clothes.auth.service;

import com.nimbusds.oauth2.sdk.TokenRequest;
import vn.fernirx.clothes.auth.dto.request.LoginRequest;
import vn.fernirx.clothes.auth.dto.request.RegisterRequest;
import vn.fernirx.clothes.auth.dto.request.ResendOtpRequest;
import vn.fernirx.clothes.auth.dto.request.VerifyOtpRequest;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);

    void register(RegisterRequest request);

    TokenResponse verifyOtp(VerifyOtpRequest request);

    void resendOtp(ResendOtpRequest request);
}
