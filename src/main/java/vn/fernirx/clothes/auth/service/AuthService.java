package vn.fernirx.clothes.auth.service;

import vn.fernirx.clothes.auth.dto.request.LoginRequest;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
}
