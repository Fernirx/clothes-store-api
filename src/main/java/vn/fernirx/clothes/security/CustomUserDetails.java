package vn.fernirx.clothes.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NullMarked
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final boolean active;
    private final boolean verified;
    private final boolean deleted;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return !deleted && verified;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
