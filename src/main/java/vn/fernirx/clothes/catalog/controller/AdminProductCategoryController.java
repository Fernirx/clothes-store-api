package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.AssignCategoriesRequest;
import vn.fernirx.clothes.catalog.service.ProductCategoryService;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/admin/products/{productId}/categories")
@RequiredArgsConstructor
public class AdminProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PutMapping
    @Operation(
            summary = "Cập nhật danh mục sản phẩm",
            description = "Cập nhật hàng loạt danh mục của một sản phẩm dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<Void>> assignCategories(
            @PathVariable Long productId,
            @Valid @RequestBody AssignCategoriesRequest request) {
        productCategoryService.assignCategories(productId, request);
        return ResponseEntity.ok(SuccessResponse.of("Product updated successfully"));
    }
}
