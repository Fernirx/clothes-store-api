package vn.fernirx.clothes.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.auth.dto.response.TokenResponse;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.enums.UserRole;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.security.JwtProvider;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.entity.UserProfile;
import vn.fernirx.clothes.user.exception.UserDeletedException;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.repository.UserRepository;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName  = oAuth2User.getAttribute("family_name");
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmailIncludeDeleted(email)
                .map(u -> {
                    if (u.isDeleted())
                        throw new UserDeletedException();
                    return u;
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .provider(Provider.GOOGLE)
                            .role(UserRole.USER)
                            .verified(true)
                            .build();
                    return userRepository.save(newUser);
                });

        userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserProfile userProfile = UserProfile.builder()
                            .user(user)
                            .firstName(firstName)
                            .lastName(lastName)
                            .build();
                    return userProfileRepository.save(userProfile);
                });

        String accessToken = jwtProvider.generateAccessTokenForOAuth2(
                user.getId(),
                user.getEmail(),
                Set.of("ROLE_" + user.getRole())
        );
        String refreshToken = jwtProvider.generateRefreshTokenForOAuth2(
                user.getId(),
                user.getEmail()
        );
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        clearAuthenticationAttributes(request);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(),
                SuccessResponse.of("Login successful", tokenResponse)
        );
    }
}
