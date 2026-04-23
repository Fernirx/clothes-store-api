package vn.fernirx.clothes.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.AppException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.order.entity.Order;
import vn.fernirx.clothes.order.repository.OrderRepository;
import vn.fernirx.clothes.payment.dto.PaymentRequest;
import vn.fernirx.clothes.payment.dto.response.PaymentResponse;
import vn.fernirx.clothes.payment.entity.Payment;
import vn.fernirx.clothes.payment.enums.PaymentStatus;
import vn.fernirx.clothes.payment.provider.vnpay.VNPayProvider;
import vn.fernirx.clothes.payment.repository.PaymentRepository;
import vn.fernirx.clothes.payment.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private static final String VNPAY_SUCCESS_CODE = "00";

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final VNPayProvider vnPayProvider;

    @Override
    @Transactional
    public String initiate(PaymentRequest request, String ipAddress) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order"));

        if (paymentRepository.existsByOrder_IdAndStatus(order.getId(), PaymentStatus.SUCCESS)) {
            throw new AppException(ErrorCode.IN_USE, "Order đã được thanh toán thành công");
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        return vnPayProvider.buildPaymentUrl(request, ipAddress);
    }

    @Override
    public PaymentStatus handleReturn(Map<String, String> params) {
        if (!vnPayProvider.verifySignature(params)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Chữ ký VNPay không hợp lệ");
        }

        String txnRef = params.get("vnp_TxnRef");
        Long orderId = extractOrderId(txnRef);

        Payment payment = paymentRepository.findTopByOrder_IdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment"));

        return payment.getStatus();
    }

    @Override
    @Transactional
    public Map<String, String> handleIpn(Map<String, String> params) {
        if (!vnPayProvider.verifySignature(params)) {
            return ipnResponse("97", "Invalid Signature");
        }

        String txnRef = params.get("vnp_TxnRef");
        if (txnRef == null) {
            return ipnResponse("01", "Order not found");
        }

        Long orderId;
        try {
            orderId = extractOrderId(txnRef);
        } catch (Exception e) {
            return ipnResponse("01", "Invalid TxnRef");
        }

        Payment payment = paymentRepository.findTopByOrder_IdOrderByCreatedAtDesc(orderId)
                .orElse(null);
        if (payment == null) {
            return ipnResponse("01", "Order not found");
        }

        // Đã xử lý trước đó — idempotent
        if (payment.getStatus() != PaymentStatus.PENDING) {
            return ipnResponse("02", "Order already confirmed");
        }

        String vnpAmount = params.get("vnp_Amount");
        if (vnpAmount != null) {
            BigDecimal receivedAmount = new BigDecimal(vnpAmount).divide(BigDecimal.valueOf(100));
            if (payment.getAmount().compareTo(receivedAmount) != 0) {
                return ipnResponse("04", "Invalid Amount");
            }
        }

        String responseCode = params.getOrDefault("vnp_ResponseCode", "");
        boolean success = VNPAY_SUCCESS_CODE.equals(responseCode);

        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment.setTransactionId(params.get("vnp_TransactionNo"));
        payment.setResponseCode(responseCode);
        payment.setResponseMessage(params.get("vnp_OrderInfo"));
        if (success) {
            payment.setPaidAt(LocalDateTime.now());
        }
        paymentRepository.save(payment);

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order"));
        order.setPaymentStatus(vn.fernirx.clothes.order.enums.PaymentStatus.FAILED);

        return ipnResponse("00", "Confirm Success");
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getOrder().getId(),
                p.getAmount(),
                p.getStatus(),
                p.getTransactionId(),
                p.getResponseCode(),
                p.getResponseMessage(),
                p.getPaidAt()
        );
    }

    private Map<String, String> ipnResponse(String code, String message) {
        return Map.of("RspCode", code, "Message", message);
    }

    private Long extractOrderId(String txnRef) {
        if (txnRef == null || txnRef.isBlank()) {
            throw new IllegalArgumentException("TxnRef is empty");
        }

        String[] parts = txnRef.split("_");

        if (parts.length == 0) {
            throw new IllegalArgumentException("Invalid TxnRef format");
        }

        return Long.parseLong(parts[0]);
    }
}