package vn.fernirx.clothes.payment.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "vnpay")
public class VnpayProperties {
    /** Mã website (Terminal ID) do VNPay cấp */
    @NotBlank
    private String tmnCode;

    /** Chuỗi bí mật dùng để ký và xác thực checksum */
    @NotBlank
    private String hashSecret;

    /** URL cổng thanh toán VNPay */
    @NotBlank
    private String paymentUrl;

    /** URL VNPay redirect về sau khi thanh toán xong */
    @NotBlank
    private String returnUrl;

    /** URL VNPay gọi để thông báo kết quả giao dịch (IPN) */
    @NotBlank
    private String ipnUrl;

    /** Phiên bản API VNPay (vd: 2.1.0) */
    @NotBlank
    private String version;

    /** Lệnh thanh toán (vd: pay) */
    @NotBlank
    private String command;

    /** Mã tiền tệ (vd: VND) */
    @NotBlank
    private String currCode;

    /** Ngôn ngữ hiển thị trên trang VNPay (vd: vn, en) */
    @NotBlank
    private String locale;

    /** Thời gian hết hạn của link thanh toán (phút) */
    @Positive
    private int expireMinutes;
}