package vn.fernirx.clothes.user.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.user.dto.request.ChangePasswordRequest;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
import vn.fernirx.clothes.user.dto.request.UpdateUserRequest;
import vn.fernirx.clothes.user.dto.request.UserFilterRequest;
import vn.fernirx.clothes.user.dto.response.UserResponse;

public interface UserService {
    PageResponse<UserResponse> getAll(Integer page, Integer size, String sortBy, String sortDir, UserFilterRequest filter);

    UserResponse getUserById(Long id);

    UserResponse createUser(CreateUserRequest createUserRequest);

    void softDeleteById(Long id);

    void restoreById(Long id);

    void hardDeleteById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void resetPassword(Long id);

    void changePassword(Long id, ChangePasswordRequest request);
}