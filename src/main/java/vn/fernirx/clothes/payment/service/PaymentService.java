package vn.fernirx.clothes.payment.service;

import vn.fernirx.clothes.payment.dto.PaymentRequest;
import vn.fernirx.clothes.payment.dto.response.PaymentResponse;

import java.util.Map;

public interface PaymentService {
    String initiate(PaymentRequest request, String ipAddress);
    PaymentResponse handleReturn(Map<String, String> params);
    Map<String, String> handleIpn(Map<String, String> params);
}