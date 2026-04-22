package vn.fernirx.clothes.payment.provider.vnpay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.payment.config.VnpayProperties;
import vn.fernirx.clothes.payment.dto.PaymentRequest;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
public class VNPayClient {

    private static final DateTimeFormatter VNPAY_DATE = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final VnpayProperties properties;

    public Map<String, String> buildBaseParams(PaymentRequest request, String ipAddress) {
        LocalDateTime now = LocalDateTime.now();

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version",    properties.getVersion());
        params.put("vnp_Command",    properties.getCommand());
        params.put("vnp_TmnCode",    properties.getTmnCode());
        params.put("vnp_Amount",     request.amount().multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
        params.put("vnp_CurrCode",   properties.getCurrCode());
        params.put("vnp_TxnRef",     String.valueOf(request.orderId()));
        params.put("vnp_OrderInfo",  "Thanh toan don hang " + request.orderId());
        params.put("vnp_OrderType",  "other");
        params.put("vnp_Locale",     properties.getLocale());
        params.put("vnp_ReturnUrl",  properties.getReturnUrl());
        params.put("vnp_IpAddr",     ipAddress);
        params.put("vnp_CreateDate", now.format(VNPAY_DATE));
        params.put("vnp_ExpireDate", now.plusMinutes(properties.getExpireMinutes()).format(VNPAY_DATE));
        return params;
    }

    public String buildUrl(Map<String, String> params, String hash) {
        StringJoiner joiner = new StringJoiner("&");
        params.forEach((k, v) ->
            joiner.add(URLEncoder.encode(k, StandardCharsets.US_ASCII)
                + "=" + URLEncoder.encode(v, StandardCharsets.US_ASCII))
        );
        joiner.add("vnp_SecureHash=" + URLEncoder.encode(hash, StandardCharsets.US_ASCII));
        return properties.getPaymentUrl() + "?" + joiner;
    }
}