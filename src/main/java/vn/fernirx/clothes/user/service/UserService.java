package vn.fernirx.clothes.user.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
import vn.fernirx.clothes.user.dto.response.UserResponse;

public interface UserService {
    PageResponse<UserResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);
    UserResponse getUserById(Long id);
    UserResponse createUser(CreateUserRequest createUserRequest);
    void softDeleteById(Long id);
}