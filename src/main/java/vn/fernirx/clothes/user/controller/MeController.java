package vn.fernirx.clothes.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.user.dto.request.ChangePasswordRequest;
import vn.fernirx.clothes.user.dto.request.UpdateProfileRequest;
import vn.fernirx.clothes.user.dto.request.UpdateShippingRequest;
import vn.fernirx.clothes.user.dto.request.UploadAvatarRequest;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;
import vn.fernirx.clothes.user.service.UserProfileService;
import vn.fernirx.clothes.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@Tag(name = "My Profile API", description = "Các API thao tác thông tin cá nhân của người dùng hiện tại")
public class MeController {
    private final UserProfileService userProfileService;
    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Lấy thông tin cá nhân",
            description = "Lấy thông tin profile của người dùng hiện tại"
    )
    public ResponseEntity<SuccessResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse data = userProfileService.getMyProfile(userDetails.getId());
        return ResponseEntity.ok(SuccessResponse.of(
                "User profile retrieved successfully",
                data
        ));
    }

    @PatchMapping
    @Operation(
            summary = "Cập nhật thông tin cá nhân",
            description = "Cập nhật thông tin cơ bản của người dùng hiện tại"
    )
    public ResponseEntity<SuccessResponse<UserProfileResponse>> updateUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        UserProfileResponse data =
                userProfileService.updateUserProfile(userDetails.getId(), updateProfileRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "User profile updated successfully",
                data
        ));
    }

    @PatchMapping("/shipping")
    @Operation(
            summary = "Cập nhật địa chỉ giao hàng",
            description = "Cập nhật thông tin giao hàng mặc định của người dùng hiện tại"
    )
    public ResponseEntity<SuccessResponse<UserProfileResponse>> updateShipping(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateShippingRequest updateShippingRequest) {
        UserProfileResponse data =
                userProfileService.updateShipping(userDetails.getId(), updateShippingRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "User shipping updated successfully",
                data
        ));
    }

    @PatchMapping(value = "/avatar")
    @Operation(
            summary = "Cập nhật avatar",
            description = "Cập nhật ảnh đại diện cho người dùng hiện tại"
    )
    public ResponseEntity<SuccessResponse<Void>> updateUserAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UploadAvatarRequest request) {
        userProfileService.updateAvatar(userDetails.getId(), request);
        return ResponseEntity.ok(SuccessResponse.of("Avatar updated successfully"));
    }

    @PostMapping("/change-password")
    @Operation(
            summary = "Đổi mật khẩu",
            description = "Người dùng tự đổi mật khẩu bằng cách cung cấp mật khẩu cũ và mật khẩu mới"
    )
    public ResponseEntity<SuccessResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userDetails.getId(), changePasswordRequest);
        return ResponseEntity.ok(SuccessResponse.of("Password changed successfully"));
    }
}
