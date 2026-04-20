package vn.fernirx.clothes.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
import vn.fernirx.clothes.user.dto.request.UpdateUserRequest;
import vn.fernirx.clothes.user.dto.request.UserFilterRequest;
import vn.fernirx.clothes.user.dto.response.UserResponse;
import vn.fernirx.clothes.user.service.UserService;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Users API", description = "Các API người dùng")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách người dùng",
            description = "Lấy danh sách tất cả người dùng với phân trang, sắp xếp và lọc theo field"
    )
    public ResponseEntity<SuccessResponse<PageResponse<UserResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            @ParameterObject @ModelAttribute UserFilterRequest filter) {
        PageResponse<UserResponse> data = userService.getAll(page, size, sortBy, sortDir, filter);
        return ResponseEntity.ok(SuccessResponse.of(
                "Users retrieved successfully",
                data
        ));
    }

    @GetMapping("/trash")
    @Operation(
            summary = "Lấy danh sách người dùng đã xóa",
            description = "Lấy danh sách tất cả người dùng đã xóa với phân trang, sắp xếp"
    )
    public ResponseEntity<SuccessResponse<PageResponse<UserResponse>>> getDeletedTrue(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        PageResponse<UserResponse> data = userService.getDeletedTrue(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of(
                "Users retrieved successfully",
                data
        ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy chi tiết người dùng",
            description = "Lấy chi tiết một người dùng theo ID"
    )
    public ResponseEntity<SuccessResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(SuccessResponse.of(
                "User retrieved successfully",
                userResponse
        ));
    }

    @PostMapping
    @Operation(
            summary = "Tạo người dùng mới",
            description = "Tạo một người dùng mới"
    )
    public ResponseEntity<SuccessResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("User created successfully", userResponse));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa người dùng",
            description = "Thực hiện xóa mềm (soft delete) người dùng theo ID"
    )
    public ResponseEntity<SuccessResponse<Void>> deleteUserById(@PathVariable Long id) {
        userService.softDeleteById(id);
        return ResponseEntity.ok(SuccessResponse.of("User delete successfully"));
    }

    @DeleteMapping("/{id}/hard")
    @Operation(
            summary = "Xóa cứng người dùng",
            description = "Thực hiện xóa cứng người dùng theo ID"
    )
    public ResponseEntity<SuccessResponse<Void>> hardDeleteById(@PathVariable Long id) {
        userService.hardDeleteById(id);
        return ResponseEntity.ok(SuccessResponse.of("User delete successfully"));
    }

    @PatchMapping("/{id}/restore")
    @Operation(
            summary = "Khôi phục người dùng đã xóa mềm",
            description = "Thực hiện khôi phục người dùng theo ID"
    )
    public ResponseEntity<SuccessResponse<Void>> restoreById(@PathVariable Long id) {
        userService.restoreById(id);
        return ResponseEntity.ok(SuccessResponse.of("User restored successfully"));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Cập nhật người dùng",
            description = "Cập nhật thông tin người dùng theo ID. Chỉ cập nhật các field được truyền lên"
    )
    public ResponseEntity<SuccessResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "User updated successfully",
                userResponse
        ));
    }

    @PostMapping("/{id}/reset-password")
    @Operation(
            summary = "Reset mật khẩu",
            description = "Đặt lại mật khẩu cho người dùng theo ID. Sẽ sinh mật khẩu mới và gửi email"
    )
    public ResponseEntity<SuccessResponse<Void>> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return ResponseEntity.ok(SuccessResponse.of("Password reset successfully"));
    }
}