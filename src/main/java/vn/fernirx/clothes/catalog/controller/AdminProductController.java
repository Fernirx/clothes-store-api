package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.AdminProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.request.CreateProductRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateProductRequest;
import vn.fernirx.clothes.catalog.dto.response.AdminProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.AdminProductSummaryResponse;
import vn.fernirx.clothes.catalog.service.ProductService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/admin/products")
@AllArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách sản phẩm",
            description = "Lấy danh sách tất cả sản phẩm với phân trang và sắp xếp"
    )
    public ResponseEntity<SuccessResponse<PageResponse<AdminProductSummaryResponse>>> getAllByActiveTrue(
            @ParameterObject @ModelAttribute AdminProductFilterRequest filter,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        PageResponse<AdminProductSummaryResponse> data = productService.getProductsForAdmin(page, size, sortBy, sortDir, filter);
        return ResponseEntity.ok(SuccessResponse.of("Products retrieved successfully", data));
    }

    @Operation(
            summary = "Lấy product theo id",
            description = "Lấy thông tin chi tiết của một product dựa trên id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<AdminProductDetailResponse>> getById(@PathVariable Long id) {
        AdminProductDetailResponse data = productService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product retrieved successfully", data));
    }

    @Operation(
            summary = "Tạo mới sản phẩm",
            description = "Tạo mới một sản phẩm với thông tin được cung cấp"
    )
    @PostMapping
    public ResponseEntity<SuccessResponse<AdminProductDetailResponse>> create(
            @Valid @RequestBody CreateProductRequest request) {
        AdminProductDetailResponse data = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product created successfully", data));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Cập nhật sản phẩm",
            description = "Cập nhật thông tin của một sản phẩm dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<AdminProductDetailResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        AdminProductDetailResponse data = productService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa sản phẩm",
            description = "Xóa một sản phẩm dựa trên ID"
    )
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product deleted successfully"));
    }
}
