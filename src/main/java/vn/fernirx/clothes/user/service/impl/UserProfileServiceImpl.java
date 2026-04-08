package vn.fernirx.clothes.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.integration.storage.CloudinaryService;
import vn.fernirx.clothes.user.dto.request.UpdateProfileRequest;
import vn.fernirx.clothes.user.dto.response.UserProfileResponse;
import vn.fernirx.clothes.user.entity.UserProfile;
import vn.fernirx.clothes.user.mapper.UserProfileMapper;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.service.UserProfileService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest req) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));
        userProfileMapper.updateUserProfile(req, userProfile);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    public String updateAvatar(Long userId, MultipartFile avatar) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));

        if (userProfile.getAvatarPublicId() != null) {
            cloudinaryService.deleteImage(userProfile.getAvatarPublicId());
        }

        Map<String, String> result = cloudinaryService.uploadProfileAvatar(userId, avatar);
        userProfile.setAvatarUrl(result.get("url"));
        userProfile.setAvatarPublicId(result.get("publicId"));
        userProfileRepository.save(userProfile);
        return userProfile.getAvatarUrl();
    }
}
