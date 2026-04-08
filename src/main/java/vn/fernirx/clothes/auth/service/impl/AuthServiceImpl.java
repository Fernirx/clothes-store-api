package vn.fernirx.clothes.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.auth.dto.request.LoginRequest;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.exception.AccountDisabledException;
import vn.fernirx.clothes.auth.exception.InvalidCredentialsException;
import vn.fernirx.clothes.auth.service.AuthService;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.security.JwtProvider;
import vn.fernirx.clothes.user.service.impl.CustomUserDetailsServiceImpl;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (DisabledException e) {
            throw new AccountDisabledException();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        return new TokenResponse(accessToken, refreshToken);
    }
}
