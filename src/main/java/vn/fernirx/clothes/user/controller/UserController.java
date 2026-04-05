package vn.fernirx.clothes.user.controller;

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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(SuccessResponse.of(
                "User retrieved successfully",
                userResponse
        ));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("User created successfully", userResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteUserById(@PathVariable Long id) {
        userService.softDeleteById(id);
        return ResponseEntity.ok(SuccessResponse.of("User delete successfully"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(SuccessResponse.of(
                "User updated successfully",
                userResponse
        ));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SuccessResponse<Void>> updateUserStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        userService.updateActiveStatus(id, active);
        return ResponseEntity.ok(SuccessResponse.of("User status updated successfully"));
    }
}