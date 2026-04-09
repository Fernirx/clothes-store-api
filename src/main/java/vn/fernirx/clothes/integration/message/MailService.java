package vn.fernirx.clothes.integration.message;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

    @Async
    public void sendVerifyEmailOtp(String to, String username, String otpCode, int expiryMinutes) {
        String subject = "Clothes - Mã Xác Thực Email";
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("otpCode", otpCode);
        context.setVariable("expiryMinutes", expiryMinutes);
        String htmlContent = templateEngine.process ("mail/verify-email-otp", context);
        sendMail(to, subject, htmlContent);
    }

    @Async
    public void sendForgotPasswordOtp(String to, String username, String otpCode, Object expiryMinutes) {
        String subject = "Clothes - Mã Xác Thực Đặt Lại Mật Khẩu";
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("otpCode", otpCode);
        context.setVariable("expiryMinutes", expiryMinutes);
        String htmlContent = templateEngine.process("mail/forgot-password-otp", context);
        sendMail(to, subject, htmlContent);
    }

    @Async
    public void sendAdminResetPassword(String to, String username, String password) {
        String subject = "Clothes - Mật khẩu mới";
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("newPassword", password);
        String htmlContent = templateEngine.process("mail/admin-reset-password", context);
        sendMail(to, subject, htmlContent);
    }
}
