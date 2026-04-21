package vn.fernirx.clothes.user.init;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.enums.UserRole;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties properties;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) throws Exception {
        if (userRepository.existsByEmailIncludeDeleted(properties.getEmail()) == 0) {
            User admin = User.builder()
                    .email(properties.getEmail())
                    .passwordHash(passwordEncoder.encode(properties.getPassword()))
                    .provider(Provider.LOCAL)
                    .role(UserRole.ADMIN)
                    .active(true)
                    .verified(true)
                    .build();

            userRepository.save(admin);
        } else {
            User user = userRepository.findByEmailIncludeDeleted(properties.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!passwordEncoder.matches(properties.getPassword(), user.getPasswordHash())) {
                user.setPasswordHash(passwordEncoder.encode(properties.getPassword()));
            }
            user.setRole(UserRole.ADMIN);
            user.setDeleted(false);
            userRepository.save(user);
        }
    }
}
