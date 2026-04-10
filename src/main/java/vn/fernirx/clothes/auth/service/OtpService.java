package vn.fernirx.clothes.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.fernirx.clothes.auth.dto.OTPData;
import vn.fernirx.clothes.auth.enums.OtpPurpose;
import vn.fernirx.clothes.auth.exception.OTPException;
import vn.fernirx.clothes.common.exception.CacheSerializationException;
import vn.fernirx.clothes.config.OTPProperties;
import vn.fernirx.clothes.integration.message.MailService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final OTPProperties otpProperties;

    private static final String OTP_KEY      = "otp:%s:%s";
    private static final String COOLDOWN_KEY = "otp:cd:%s:%s";

    public void sendOtp(String email, String name, OtpPurpose purpose) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(COOLDOWN_KEY.formatted(purpose.name().toLowerCase(), email))))
            throw OTPException.resendCooldownExceeded();

        String rawOtp = generateOtp();
        OTPData otpData = OTPData.builder()
                .hashedOtp(passwordEncoder.encode(rawOtp))
                .attempts(0)
                .lastSentAt(Instant.now())
                .build();

        set(OTP_KEY.formatted(purpose.name().toLowerCase(), email), otpData, otpProperties.getTtl(), TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(COOLDOWN_KEY.formatted(purpose.name().toLowerCase(), email), "1", otpProperties.getResendCooldown(), TimeUnit.SECONDS);

        if (purpose == OtpPurpose.REGISTER)
            mailService.sendVerifyEmailOtp(email, name, rawOtp, otpProperties.getTtl());
        else mailService.sendForgotPasswordOtp(email, name, rawOtp, otpProperties.getTtl());
    }

    public void verifyOtp(String email, String rawOtp, OtpPurpose purpose) {
        String otpKey = OTP_KEY.formatted(purpose.name().toLowerCase(), email);
        OTPData otpData = get(otpKey)
                .orElseThrow(OTPException::validationFailed);

        if (otpData.getAttempts() >= otpProperties.getMaxAttempts()) {
            redisTemplate.delete(otpKey);
            throw OTPException.maxAttemptsExceeded();
        }

        if (!passwordEncoder.matches(rawOtp, otpData.getHashedOtp())) {
            otpData.setAttempts(otpData.getAttempts() + 1);
            Long remainingTtl = redisTemplate.getExpire(otpKey, TimeUnit.SECONDS);
            set(otpKey, otpData, remainingTtl != null ? remainingTtl : otpProperties.getTtl() * 60L, TimeUnit.SECONDS);
            throw OTPException.validationFailed();
        }

        redisTemplate.delete(otpKey);
    }

    private void set(String key, OTPData value, long ttl, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttl, unit);
        } catch (JsonProcessingException e) {
            throw new CacheSerializationException("Cannot serialize: " + key, e);
        }
    }

    private Optional<OTPData> get(String key) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) return Optional.empty();
            return Optional.of(objectMapper.readValue(json, OTPData.class));
        } catch (JsonProcessingException e) {
            throw new CacheSerializationException("Cannot deserialize: " + key, e);
        }
    }

    private String generateOtp() {
        int otp = new SecureRandom().nextInt((int) Math.pow(10, otpProperties.getLength()));
        return String.format("%0" + otpProperties.getLength() + "d", otp);
    }
}
