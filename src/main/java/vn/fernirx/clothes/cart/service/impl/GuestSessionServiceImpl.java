package vn.fernirx.clothes.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.cart.entity.GuestSession;
import vn.fernirx.clothes.cart.repository.GuestSessionRepository;
import vn.fernirx.clothes.cart.service.GuestSessionService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GuestSessionServiceImpl implements GuestSessionService {
    private final GuestSessionRepository guestSessionRepository;

    @Override
    public GuestSession getOrCreate(String guestToken) {
        if (guestToken != null) {
            return guestSessionRepository.findByGuestToken(guestToken)
                    .filter(s -> s.getExpiresAt().isAfter(LocalDateTime.now()))
                    .orElseGet(this::createNew);
        }
        return createNew();
    }

    private GuestSession createNew() {
        GuestSession guestSession = GuestSession.builder()
                .guestToken(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .lastActive(LocalDateTime.now())
                .build();
        return guestSessionRepository.save(guestSession);
    }
}
