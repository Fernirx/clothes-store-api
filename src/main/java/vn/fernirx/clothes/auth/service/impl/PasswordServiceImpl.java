package vn.fernirx.clothes.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.auth.dto.request.ForgotPasswordRequest;
import vn.fernirx.clothes.auth.dto.request.ResendOtpRequest;
import vn.fernirx.clothes.auth.dto.request.ResetPasswordRequest;
import vn.fernirx.clothes.auth.dto.request.VerifyOtpRequest;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.enums.OtpPurpose;
import vn.fernirx.clothes.auth.service.OtpService;
import vn.fernirx.clothes.auth.service.PasswordService;
import vn.fernirx.clothes.common.enums.BlacklistReason;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.exception.TokenException;
import vn.fernirx.clothes.security.JwtProvider;
import vn.fernirx.clothes.security.token.TokenBlacklistService;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.entity.UserProfile;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final OtpService otpService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        otpService.sendOtp(user.getEmail(), userProfile.getFirstName(), OtpPurpose.FORGOT_PASSWORD);
    }

    @Override
    public TokenResponse verifyForgotPassword(VerifyOtpRequest request) {
        otpService.verifyOtp(request.email(), request.otp(), OtpPurpose.FORGOT_PASSWORD);
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        String token = jwtProvider.generateResetPasswordToken(user.getId(), user.getEmail());
        return TokenResponse.builder()
                .resetPasswordToken(token)
                .build();
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        otpService.sendOtp(user.getEmail(), userProfile.getFirstName(), OtpPurpose.FORGOT_PASSWORD);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (tokenBlacklistService.isBlacklisted(request.resetPasswordToken()))
            throw TokenException.invalid();

        jwtProvider.validateResetPasswordToken(request.resetPasswordToken());
        String email = jwtProvider.extractEmail(request.resetPasswordToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        tokenBlacklistService.blacklist(request.resetPasswordToken(), user.getId(), BlacklistReason.REVOKED);
    }
}
