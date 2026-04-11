package vn.fernirx.clothes.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.auth.dto.request.*;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.dto.response.UserInfo;
import vn.fernirx.clothes.auth.enums.OtpPurpose;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.auth.exception.AccountDisabledException;
import vn.fernirx.clothes.auth.exception.InvalidCredentialsException;
import vn.fernirx.clothes.auth.service.AuthService;
import vn.fernirx.clothes.auth.service.OtpService;
import vn.fernirx.clothes.common.enums.UserRole;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.enums.BlacklistReason;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.security.JwtProvider;
import vn.fernirx.clothes.security.SecurityUtils;
import vn.fernirx.clothes.security.token.TokenBlacklistService;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.entity.UserProfile;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.repository.UserRepository;
import vn.fernirx.clothes.user.service.impl.CustomUserDetailsServiceImpl;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

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
        UserInfo userResponse = new UserInfo(
                userDetails.getId(),
                userDetails.getEmail(),
                SecurityUtils.getAuthorities(userDetails)
        );
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String email = jwtProvider.extractEmail(request.refreshToken());
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String accessToken = jwtProvider.refreshAccessToken(request.refreshToken(), userDetails);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmailIncludeDeleted(request.email()) == 1) {
            throw new ResourceAlreadyExistsException("User");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .provider(Provider.LOCAL)
                .role(UserRole.USER)
                .active(true)
                .build();
        userRepository.save(user);
        UserProfile userProfile = UserProfile.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .user(user)
                .build();
        userProfileRepository.save(userProfile);
        otpService.sendOtp(request.email(), request.firstName(), OtpPurpose.REGISTER);
    }

    @Override
    public TokenResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        otpService.verifyOtp(request.email(), request.otp(), OtpPurpose.REGISTER);
        user.setVerified(true);
        userRepository.save(user);
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(request.email());
        String accessToken = jwtProvider.generateAccessToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        UserInfo userResponse = new UserInfo(
                userDetails.getId(),
                userDetails.getEmail(),
                SecurityUtils.getAuthorities(userDetails)
        );
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        long userId = Long.parseLong(jwtProvider.extractSubject(accessToken));
        tokenBlacklistService.blacklist(accessToken, userId, BlacklistReason.LOGOUT);
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));
        otpService.sendOtp(request.email(), userProfile.getFirstName(), OtpPurpose.REGISTER);
    }
}
