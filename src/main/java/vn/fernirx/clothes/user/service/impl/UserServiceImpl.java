package vn.fernirx.clothes.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
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

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return userMapper.toDto(user);
    }
}
