package vn.fernirx.clothes.security.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlacklistCleanupJob {
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Scheduled(cron = "${application.security.token.blacklist.cleanup-cron:0 0 3 * * *}")
    public void cleanupExpiredTokens() {
        tokenBlacklistRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        log.info("Đã dọn dẹp các token hết hạn trong blacklist");
    }
}
