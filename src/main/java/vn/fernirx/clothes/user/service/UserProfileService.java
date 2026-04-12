package vn.fernirx.clothes.user.service;

import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.user.dto.request.UpdateProfileRequest;
import vn.fernirx.clothes.user.dto.request.UpdateShippingRequest;
import vn.fernirx.clothes.user.dto.request.UploadAvatarRequest;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getMyProfile(Long userId);

    UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest req);

    UserProfileResponse updateShipping(Long userId, UpdateShippingRequest req);

    void updateAvatar(Long userId, UploadAvatarRequest req);
}
