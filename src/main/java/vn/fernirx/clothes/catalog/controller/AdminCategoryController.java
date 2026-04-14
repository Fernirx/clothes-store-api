package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.CreateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.catalog.service.CategoryService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "Các endpoint để quản lý danh mục sản phẩm")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách tất cả danh mục",
            description = "Lấy danh sách tất cả danh mục với phân trang và sắp xếp tùy chọn"
    )
    public ResponseEntity<SuccessResponse<PageResponse<CategoryResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        PageResponse<CategoryResponse> data = categoryService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Categories retrieved successfully", data));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy thông tin danh mục theo ID",
            description = "Lấy thông tin chi tiết của một danh mục dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<CategoryResponse>> getById(@PathVariable Long id) {
        CategoryResponse data = categoryService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Category retrieved successfully", data));
    }

    @PostMapping
    @Operation(
            summary = "Tạo mới danh mục",
            description = "Tạo mới một danh mục sản phẩm với thông tin được cung cấp"
    )
    public ResponseEntity<SuccessResponse<CategoryResponse>> create(
            @Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse data = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Category created successfully", data));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Cập nhật danh mục",
            description = "Cập nhật thông tin của một danh mục sản phẩm dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse data = categoryService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Category updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa danh mục",
            description = "Xóa một danh mục sản phẩm dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Category deleted successfully"));
    }
}
