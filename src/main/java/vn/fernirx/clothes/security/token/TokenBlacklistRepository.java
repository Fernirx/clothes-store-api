package vn.fernirx.clothes.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByToken(String token);

    @Transactional
    void deleteByExpiresAtBefore(LocalDateTime threshold);
}