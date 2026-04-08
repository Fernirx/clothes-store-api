package vn.fernirx.clothes.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.mapper.UserMapper;
import vn.fernirx.clothes.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return userMapper.toCustomUserDetails(user);
    }
}
