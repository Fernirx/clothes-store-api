package vn.fernirx.clothes.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.security.CustomUserDetails;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.service.UserProfileService;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse data = userProfileService.getMyProfile(userDetails.getId());
        return ResponseEntity.ok(SuccessResponse.of(
                "User profile retrieved successfully",
                data
        ));
    }
}
