package vn.fernirx.clothes.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
import vn.fernirx.clothes.user.dto.response.UserResponse;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.mapper.UserMapper;
import vn.fernirx.clothes.user.repository.UserRepository;
import vn.fernirx.clothes.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Page<UserResponse> userResponses = userRepository.findAll(pageable)
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
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        userRepository.delete(user);
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email");
        }
    }
}