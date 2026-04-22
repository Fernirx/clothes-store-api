package vn.fernirx.clothes.payment.provider;

import vn.fernirx.clothes.payment.dto.PaymentRequest;

import java.util.Map;

public interface PaymentProvider {
    String buildPaymentUrl(PaymentRequest request, String ipAddress);
    boolean verifySignature(Map<String, String> params);
}