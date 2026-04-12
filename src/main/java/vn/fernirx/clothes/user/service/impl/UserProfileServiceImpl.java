package vn.fernirx.clothes.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.media.service.MediaService;
import vn.fernirx.clothes.user.dto.request.UpdateProfileRequest;
import vn.fernirx.clothes.user.dto.request.UpdateShippingRequest;
import vn.fernirx.clothes.user.dto.request.UploadAvatarRequest;
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
    @Transactional
    public UserProfileResponse updateShipping(Long userId, UpdateShippingRequest req) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));
        userProfileMapper.updateShipping(req, userProfile);
        userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    @Transactional
    public void updateAvatar(Long userId, UploadAvatarRequest req) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));
        userProfile.setAvatarUrl(req.url());
        userProfile.setAvatarPublicId(req.publicId());
        userProfileRepository.save(userProfile);
    }
}
