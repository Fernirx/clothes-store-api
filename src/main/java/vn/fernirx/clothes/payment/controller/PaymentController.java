package vn.fernirx.clothes.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.payment.dto.PaymentRequest;
import vn.fernirx.clothes.payment.enums.PaymentStatus;
import vn.fernirx.clothes.payment.service.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Các API xử lý thanh toán qua VNPay")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/vnpay")
    @Operation(summary = "Khởi tạo thanh toán VNPay", description = "Tạo link thanh toán VNPay từ thông tin đơn hàng. Client redirect người dùng tới URL trả về.")
    public ResponseEntity<SuccessResponse<String>> initiate(
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) {
        String ipAddress = resolveClientIp(httpRequest);
        String paymentUrl = paymentService.initiate(request, ipAddress);
        return ResponseEntity.ok(SuccessResponse.of("Tạo link thanh toán thành công", paymentUrl));
    }

    @GetMapping("/vnpay/return")
    @Operation(
            summary = "Return URL VNPay",
            description = "VNPay redirect người dùng về đây sau khi thanh toán và backend sẽ redirect tiếp sang frontend."
    )
    public RedirectView handleReturn(@RequestParam Map<String, String> params) {

        PaymentStatus status = paymentService.handleReturn(params);

        String redirectUrl = switch (status) {
            case SUCCESS -> "https://clothes.fernirx.io.vn/payment/success";
            case FAILED -> "https://clothes.fernirx.io.vn/payment/failed";
            default -> "http://localhost:3000/payment/pending";
        };

        return new RedirectView(redirectUrl);
    }

    @GetMapping("/vnpay/ipn")
    @Operation(summary = "IPN URL VNPay", description = "VNPay gọi endpoint này để xác nhận kết quả giao dịch (server-to-server). Trả về mã phản hồi theo chuẩn VNPay.")
    public ResponseEntity<Map<String, String>> handleIpn(
            @RequestParam Map<String, String> params) {

        Map<String, String> result = paymentService.handleIpn(params);
        return ResponseEntity.ok(result);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For có thể chứa chuỗi IP phân cách bởi dấu phẩy
        return ip.contains(",") ? ip.split(",")[0].trim() : ip;
    }
}