package vn.fernirx.clothes.user.service;

import vn.fernirx.clothes.user.dto.response.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getMyProfile(Long userId);
}
