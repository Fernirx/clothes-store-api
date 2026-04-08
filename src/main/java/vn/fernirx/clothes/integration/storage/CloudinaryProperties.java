package vn.fernirx.clothes.integration.storage;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "application.storage.cloudinary")
public class CloudinaryProperties {
    /** Tên cloud trong Cloudinary account. Dùng để xác định tài khoản lưu trữ media */
    @NotBlank
    private String cloudName;

    /** API key dùng để xác thực khi gọi Cloudinary API */
    @NotBlank
    private String apiKey;

    /** API secret dùng để ký request (bảo mật) Không được expose ra ngoài */
    @NotBlank
    private String apiSecret;
}
