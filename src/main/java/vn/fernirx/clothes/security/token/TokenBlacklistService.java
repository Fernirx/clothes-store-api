package vn.fernirx.clothes.security.token;

import vn.fernirx.clothes.common.enums.BlacklistReason;

public interface TokenBlacklistService {
    void blacklist(String token, Long userId, BlacklistReason reason);
    boolean isBlacklisted(String token);
}