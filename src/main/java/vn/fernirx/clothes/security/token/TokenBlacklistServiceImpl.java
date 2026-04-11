package vn.fernirx.clothes.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fernirx.clothes.common.enums.BlacklistReason;
import vn.fernirx.clothes.security.JwtProvider;
import vn.fernirx.clothes.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void blacklist(String token, Long userId, BlacklistReason reason) {
        TokenBlacklist entry = TokenBlacklist.builder()
                .token(token)
                .user(userId != null ? userRepository.getReferenceById(userId) : null)
                .reason(reason)
                .expiresAt(jwtProvider.extractExpiration(token))
                .build();
        tokenBlacklistRepository.save(entry);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}