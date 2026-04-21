package vn.fernirx.clothes.payment.provider.vnpay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.payment.dto.PaymentRequest;
import vn.fernirx.clothes.payment.provider.PaymentProvider;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VNPayProvider implements PaymentProvider {

    private final VNPayClient client;
    private final VNPaySignature signature;

    @Override
    public String buildPaymentUrl(PaymentRequest request, String ipAddress) {
        Map<String, String> params = client.buildBaseParams(request, ipAddress);
        String hash = signature.sign(params);
        return client.buildUrl(params, hash);
    }

    @Override
    public boolean verifySignature(Map<String, String> params) {
        return signature.verify(params);
    }
}