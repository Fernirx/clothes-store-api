package vn.fernirx.clothes.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.fernirx.clothes.notification.provider.MailProvider;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final MailProvider provider;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendVerifyEmailOtp(String to, String username, String otpCode, int expiryMinutes) {
        String subject = "Clothes - Mã Xác Thực Email";
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("otpCode", otpCode);
        context.setVariable("expiryMinutes", expiryMinutes);
        String htmlContent = templateEngine.process ("mail/verify-email-otp", context);
        provider.send(to, subject, htmlContent);
    }

    @Async
    public void sendForgotPasswordOtp(String to, String username, String otpCode, int expiryMinutes) {
        String subject = "Clothes - Mã Xác Thực Đặt Lại Mật Khẩu";
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("otpCode", otpCode);
        context.setVariable("expiryMinutes", expiryMinutes);
        String htmlContent = templateEngine.process("mail/forgot-password-otp", context);
        provider.send(to, subject, htmlContent);
    }

    @Async
    public void sendAdminResetPassword(String to, String username, String password) {
        String subject = "Clothes - Mật khẩu mới";
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("newPassword", password);
        String htmlContent = templateEngine.process("mail/admin-reset-password", context);
        provider.send(to, subject, htmlContent);
    }
}
