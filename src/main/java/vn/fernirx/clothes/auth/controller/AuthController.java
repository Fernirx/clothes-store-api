package vn.fernirx.clothes.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fernirx.clothes.auth.dto.request.LoginRequest;
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
}
