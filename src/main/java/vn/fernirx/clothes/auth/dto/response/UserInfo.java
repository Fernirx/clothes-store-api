package vn.fernirx.clothes.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserInfo(
        Long id,
        String email,
        Set<String> roles
) {}
