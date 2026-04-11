package vn.fernirx.clothes.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;
import vn.fernirx.clothes.common.util.PasswordUtil;
import vn.fernirx.clothes.integration.message.MailService;
import vn.fernirx.clothes.user.dto.request.ChangePasswordRequest;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
import vn.fernirx.clothes.user.dto.request.UpdateUserRequest;
import vn.fernirx.clothes.user.dto.request.UserFilterRequest;
import vn.fernirx.clothes.user.dto.response.UserResponse;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.entity.UserProfile;
import vn.fernirx.clothes.user.exception.InvalidPasswordException;
import vn.fernirx.clothes.user.mapper.UserMapper;
import vn.fernirx.clothes.user.repository.UserProfileRepository;
import vn.fernirx.clothes.user.repository.UserRepository;
import vn.fernirx.clothes.user.repository.UserSpecification;
import vn.fernirx.clothes.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAll(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir,
            UserFilterRequest filter) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Specification<User> userSpecification = UserSpecification.build(filter);
        Page<UserResponse> userResponses = userRepository.findAll(userSpecification, pageable)
                .map(userMapper::toDto);
        return PageResponse.of(userResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getDeletedTrue(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<UserResponse> userResponses = userRepository.findAllDeletedTrue(pageable)
                .map(userMapper::toDto);
        return PageResponse.of(userResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        validateEmailNotExists(createUserRequest.email());

        User user = userMapper.toUserFromCreateRequest(createUserRequest);
        user.setPasswordHash(passwordEncoder.encode(createUserRequest.password()));
        user.setProvider(Provider.LOCAL);
        userRepository.save(user);

        String firstName = user.getEmail().split("@")[0];
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(firstName);
        userProfile.setUser(user);
        userProfileRepository.save(userProfile);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void restoreById(Long id) {
        User user = userRepository.findByIdIncludeDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        user.setDeleted(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void hardDeleteById(Long id) {
        User user = userRepository.findByIdIncludeDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        userRepository.hardDeleteById(user.getId());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        if (request.email() != null) {
            validateEmailNotExists(request.email());
            user.setEmail(request.email());
        }
        if (request.role() != null) {
            user.setRole(request.role());
        }
        if (request.active() != null) {
            user.setActive(request.active());
        }
        if (request.verified() != null) {
            user.setVerified(request.verified());
        }
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        UserProfile userProfile = userProfileRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile"));
        String newPassword = PasswordUtil.generateRandomPassword();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        mailService.sendAdminResetPassword(
                user.getEmail(),
                userProfile.getFirstName(),
                newPassword
        );
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw InvalidPasswordException.oldPasswordIncorrect();
        }
        if (!passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
            throw InvalidPasswordException.reuseNotAllowed();
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmailIncludeDeleted(email) == 1) {
            throw new ResourceAlreadyExistsException("Email");
        }
    }
}