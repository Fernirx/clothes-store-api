package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.CreateProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;
import vn.fernirx.clothes.catalog.service.ProductImageService;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/api/admin/products/{productId}/images")
@RequiredArgsConstructor
public class AdminProductImageController {
    private final ProductImageService productImageService;

    @Operation(
            summary = "Tạo mới ảnh biến thể",
            description = "Tạo mới một ảnh biến thể với thông tin được cung cấp và id của sản phẩm"
    )
    @PostMapping
    public ResponseEntity<SuccessResponse<ProductImageResponse>> create(
            @PathVariable Long productId,
            @Valid @RequestBody CreateProductImageRequest request) {
        ProductImageResponse data = productImageService.create(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product image created successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa ảnh biến thể",
            description = "Xóa một ảnh biến thể dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<Void>> delete(
            @PathVariable Long productId,
            @PathVariable Long id) {
        productImageService.delete(productId, id);
        return ResponseEntity.ok(SuccessResponse.of("Product image deleted successfully"));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Cập nhật ảnh chính",
            description = "Cập nhật ảnh chính của 1 màu"
    )
    public ResponseEntity<SuccessResponse<Void>> update(
            @PathVariable Long productId,
            @PathVariable Long id) {
        productImageService.setImagePrimary(productId, id);
        return ResponseEntity.ok(SuccessResponse.of("Product image updated successfully"));
    }
}
