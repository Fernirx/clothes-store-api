package vn.fernirx.clothes.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class SecurityUtils {
    public Optional<CustomUserDetails>  getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)){
            return Optional.empty();
        }
        return Optional.of((CustomUserDetails) authentication.getPrincipal());
    }

    public Optional<Long> getCurrentUserId() {
        return getCurrentUser()
                .map(CustomUserDetails::getId);
    }

    public Set<String> getCurrentUserRoles() {
        return getCurrentUser()
                .map(SecurityUtils::getAuthorities)
                .orElse(Collections.emptySet());
    }

    public Set<String> getAuthorities(CustomUserDetails user) {
        if (user == null) return Collections.emptySet();

        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
