package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.ProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;
import vn.fernirx.clothes.catalog.service.ProductImageService;
import vn.fernirx.clothes.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Product Images API", description = "Các endpoint để quản lý hình ảnh sản phẩm")
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping("/by-product/{productId}")
    @Operation(summary = "Lấy danh sách hình ảnh của một sản phẩm", description = "Trả về tất cả hình ảnh liên quan đến sản phẩm dựa trên productId")
    public ResponseEntity<SuccessResponse<List<ProductImageResponse>>> getByProductId(
            @PathVariable Long productId) {

        List<ProductImageResponse> data = productImageService.getByProductId(productId);
        return ResponseEntity.ok(SuccessResponse.of("Product images retrieved successfully", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin hình ảnh sản phẩm theo ID", description = "Trả về thông tin chi tiết của một hình ảnh sản phẩm dựa trên ID")
    public ResponseEntity<SuccessResponse<ProductImageResponse>> getById(@PathVariable Long id) {
        ProductImageResponse data = productImageService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product image retrieved successfully", data));
    }

    @PostMapping
    @Operation(summary = "Tạo mới hình ảnh sản phẩm", description = "Tạo một hình ảnh sản phẩm mới dựa trên thông tin được cung cấp trong request body")
    public ResponseEntity<SuccessResponse<ProductImageResponse>> create(
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse data = productImageService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product image created successfully", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật hình ảnh sản phẩm", description = "Cập nhật thông tin của một hình ảnh sản phẩm dựa trên ID và thông tin mới được cung cấp trong request body")
    public ResponseEntity<SuccessResponse<ProductImageResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse data = productImageService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product image updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa hình ảnh sản phẩm", description = "Xóa một hình ảnh sản phẩm dựa trên ID")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productImageService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product image deleted successfully"));
    }
}
