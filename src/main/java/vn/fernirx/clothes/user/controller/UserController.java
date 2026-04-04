package vn.fernirx.clothes.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.user.dto.request.CreateUserRequest;
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
            @RequestParam(required = false) String sortDir) {
        PageResponse<UserResponse> data = userService.getAll(page, size, sortBy, sortDir);
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
}