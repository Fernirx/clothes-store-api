package vn.fernirx.clothes.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {

    /** Khóa bí mật dùng để ký JWT */
    @NotBlank
    private String secret;

    /** Đơn vị phát hành (issuer) của JWT */
    @NotBlank
    private String issuer;

    /** Cấu hình access token */
    private AccessToken access = new AccessToken();

    /** Cấu hình refresh token */
    private RefreshToken refresh = new RefreshToken();

    /** Cấu hình token đặt lại mật khẩu */
    private ResetPasswordToken resetPassword = new ResetPasswordToken();

    @Data
    public static class AccessToken {
        /** Thời gian hết hạn của access token */
        private Duration expiration = Duration.parse("PT10M"); // mặc định 10 phút
    }

    @Data
    public static class RefreshToken {
        /** Thời gian hết hạn của refresh token */
        private Duration expiration = Duration.parse("P7D"); // mặc định 7 ngày
    }

    @Data
    public static class ResetPasswordToken {
        /** Thời gian hết hạn của token đặt lại mật khẩu */
        private Duration expiration = Duration.parse("PT15M"); // mặc định 15 phút
    }
}
