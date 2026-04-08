package vn.fernirx.clothes.user.dto.request;

public record UserFilterRequest(
        String role,
        String provider,
        Boolean active) {
}
