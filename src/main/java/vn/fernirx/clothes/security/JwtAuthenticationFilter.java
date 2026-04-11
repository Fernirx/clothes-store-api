package vn.fernirx.clothes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.fernirx.clothes.common.constant.SecurityConstants;
import vn.fernirx.clothes.common.exception.TokenException;
import vn.fernirx.clothes.handler.JwtAuthenticationEntryPoint;
import vn.fernirx.clothes.security.token.TokenBlacklistService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final TokenBlacklistService tokenBlacklistService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if  (SecurityConstants.SKIP_PATHS.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractJwtToken(request);
            if (isValidToken(token)) {
                if (tokenBlacklistService.isBlacklisted(token)) {
                    throw TokenException.invalid();
                }
                setAuthenticationContext(token, request);
            }
            filterChain.doFilter(request, response);
        } catch (TokenException ex) {
            authenticationEntryPoint.commence(request, response, ex);
        }
    }

    /* ================== PRIVATE HELPERS ================== */

    private String extractJwtToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return headerAuth.substring(SecurityConstants.BEARER_PREFIX_LENGTH);
        }
        return null;
    }

    private boolean isValidToken(String token) {
        return token != null && jwtProvider.validateAccessToken(token);
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String email = jwtProvider.extractEmail(token);
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}