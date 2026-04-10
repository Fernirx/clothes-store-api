package vn.fernirx.clothes.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {}
