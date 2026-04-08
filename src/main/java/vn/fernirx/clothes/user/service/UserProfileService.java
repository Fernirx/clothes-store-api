package vn.fernirx.clothes.user.service;

import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.user.dto.request.UpdateProfileRequest;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getMyProfile(Long userId);
    UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest req);
    String updateAvatar(Long userId, MultipartFile avatar);
}
