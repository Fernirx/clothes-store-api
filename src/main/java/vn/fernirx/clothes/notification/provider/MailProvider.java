package vn.fernirx.clothes.notification.provider;

public interface MailProvider {
    void send(String to, String subject, String htmlContent);
}
