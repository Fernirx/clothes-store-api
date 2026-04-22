package vn.fernirx.clothes.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.payment.entity.Payment;
import vn.fernirx.clothes.payment.enums.PaymentStatus;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findTopByOrder_IdOrderByCreatedAtDesc(Long orderId);
    boolean existsByOrder_IdAndStatus(Long orderId, PaymentStatus status);
}