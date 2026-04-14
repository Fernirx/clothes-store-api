package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.catalog.service.CategoryService;
import vn.fernirx.clothes.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "Các endpoint để quản lý danh mục sản phẩm")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách danh mục gốc",
            description = "Lấy danh sách các danh mục gốc"
    )
    public ResponseEntity<SuccessResponse<List<CategoryResponse>>> getRootCategories() {
        List<CategoryResponse> data = categoryService.getRootCategories();
        return ResponseEntity.ok(SuccessResponse.of("Root categories retrieved successfully", data));
    }

    @Operation(
            summary = "Lấy danh mục theo slug",
            description = "Lấy thông tin chi tiết của một danh mục dựa trên slug"
    )
    @GetMapping("/{slug}")
    public ResponseEntity<SuccessResponse<CategoryResponse>> getBySlug(@PathVariable String slug) {
        CategoryResponse data = categoryService.getBySlug(slug);
        return ResponseEntity.ok(SuccessResponse.of("Category retrieved successfully", data));
    }

    @GetMapping("/{slug}/children")
    @Operation(
            summary = "Lấy danh mục con theo slug",
            description = "Lấy danh sách các danh mục con trực tiếp của danh mục chỉ định"
    )
    public ResponseEntity<SuccessResponse<List<CategoryResponse>>> getChildrenBySlug(
            @PathVariable String slug) {
        List<CategoryResponse> data = categoryService.getChildrenBySlug(slug);
        return ResponseEntity.ok(SuccessResponse.of("Children categories retrieved successfully", data));
    }
}
