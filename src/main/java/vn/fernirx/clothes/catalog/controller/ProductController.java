package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.ProductRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductResponse;
import vn.fernirx.clothes.catalog.service.ProductService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Các endpoint để quản lý sản phẩm")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả sản phẩm", description = "Lấy danh sách tất cả sản phẩm với phân trang và sắp xếp tùy chọn")
    public ResponseEntity<SuccessResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<ProductResponse> data = productService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Products retrieved successfully", data));
    }

    @GetMapping("/active")
    @Operation(summary = "Lấy danh sách sản phẩm đang hoạt động", description = "Lấy danh sách các sản phẩm đang hoạt động với phân trang và sắp xếp tùy chọn")
    public ResponseEntity<SuccessResponse<PageResponse<ProductResponse>>> getActive(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<ProductResponse> data = productService.getActive(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Active products retrieved successfully", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin sản phẩm theo ID", description = "Lấy thông tin chi tiết của một sản phẩm dựa trên ID")
    public ResponseEntity<SuccessResponse<ProductResponse>> getById(@PathVariable Long id) {
        ProductResponse data = productService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product retrieved successfully", data));
    }

    @PostMapping
    @Operation(summary = "Tạo mới sản phẩm", description = "Tạo mới một sản phẩm với thông tin được cung cấp")
    public ResponseEntity<SuccessResponse<ProductResponse>> create(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse data = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product created successfully", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin của một sản phẩm dựa trên ID")
    public ResponseEntity<SuccessResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse data = productService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa sản phẩm", description = "Xóa một sản phẩm dựa trên ID")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product deleted successfully"));
    }
}
