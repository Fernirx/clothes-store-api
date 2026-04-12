package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UploadAvatarRequest(
        @NotBlank
        String url,

        @NotBlank
        String publicId
) {}
