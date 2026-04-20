package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.CreateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;
import vn.fernirx.clothes.catalog.service.ProductVariantService;
import vn.fernirx.clothes.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/admin/products/{productId}/variants")
@RequiredArgsConstructor
public class AdminProductVariantController {
    private final ProductVariantService productVariantService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách biến thể của sản phẩm",
            description = "Lấy danh sách tất cả biến thể của sản phẩm với id sản phẩm"
    )
    public ResponseEntity<SuccessResponse<List<ProductVariantResponse>>> getAll(
            @PathVariable Long productId
    ) {
        List<ProductVariantResponse> data = productVariantService.getAll(productId);
        return ResponseEntity.ok(SuccessResponse.of("Product Variants retrieved successfully", data));
    }

    @Operation(
            summary = "Lấy chi tiết biến thể sản phẩm",
            description = "Lấy thông tin chi tiết của một biến thể dựa trên id của sản phẩm và biến thể"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> getById(
            @PathVariable Long productId,
            @PathVariable Long id) {
        ProductVariantResponse data = productVariantService.getById(productId, id);
        return ResponseEntity.ok(SuccessResponse.of("Product Variant retrieved successfully", data));
    }

    @Operation(
            summary = "Tạo mới biến thể sản phảm",
            description = "Tạo mới một biến thể sản phẩm với thông tin được cung cấp"
    )
    @PostMapping
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> create(
            @PathVariable Long productId,
            @Valid @RequestBody CreateProductVariantRequest request) {
        ProductVariantResponse data = productVariantService.create(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product Variant created successfully", data));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Cập nhật biến thể sản phẩm",
            description = "Cập nhật thông tin của một biển thể sản phẩm dựa trên id sản phẩm và biến thể"
    )
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> update(
            @PathVariable Long productId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductVariantRequest request) {
        ProductVariantResponse data = productVariantService.update(productId, id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product Variant updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa biến thể sản phẩm",
            description = "Xóa một biến thể sản phẩm dựa trên id của sản phẩm và biến thể"
    )
    public ResponseEntity<SuccessResponse<Void>> delete(
            @PathVariable Long productId,
            @PathVariable Long id) {
        productVariantService.delete(productId, id);
        return ResponseEntity.ok(SuccessResponse.of("Product variant deleted successfully"));
    }
}
