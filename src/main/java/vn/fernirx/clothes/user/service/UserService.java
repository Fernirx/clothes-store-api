package vn.fernirx.clothes.user.service;

import vn.fernirx.clothes.user.dto.response.UserResponse;

public interface UserService {
    public UserResponse getUserById(Long id);
}
