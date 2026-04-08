package vn.fernirx.clothes.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.user.dto.request.UpdateProfileRequest;
import vn.fernirx.clothes.user.dto.request.UpdateShippingRequest;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.service.UserProfileService;
import vn.fernirx.clothes.user.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/users/me/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse data = userProfileService.getMyProfile(userDetails.getId());
        return ResponseEntity.ok(SuccessResponse.of(
                "User profile retrieved successfully",
                data
        ));
    }

    @PatchMapping
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

    @PatchMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Map<String, String>>> updateUserAvatar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("avatar") MultipartFile avatar){
        String url = userProfileService.updateAvatar(userDetails.getId(), avatar);
        return ResponseEntity.ok(SuccessResponse.of(
                "Avatar updated successfully",
                Map.of("avatarUrl", url)
        ));
    }
}
