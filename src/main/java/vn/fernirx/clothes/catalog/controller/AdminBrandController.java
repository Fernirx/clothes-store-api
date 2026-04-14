package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.CreateBrandRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateBrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.service.BrandService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/admin/brands")
@RequiredArgsConstructor
@Tag(name = "Brand API", description = "Các endpoint liên quan đến thương hiệu sản phẩm")
public class AdminBrandController {
    private final BrandService brandService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách thương hiệu",
            description = "Lấy danh sách tất cả thương hiệu với phân trang và sắp xếp"
    )
    public ResponseEntity<SuccessResponse<PageResponse<BrandResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<BrandResponse> data = brandService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Brands retrieved successfully", data));
    }

    @Operation(
            summary = "Lấy thương hiệu theo ID",
            description = "Lấy thông tin chi tiết của một thương hiệu dựa trên ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<BrandResponse>> getById(@PathVariable Long id) {
        BrandResponse data = brandService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Brand retrieved successfully", data));
    }

    @Operation(
            summary = "Tạo mới thương hiệu",
            description = "Tạo mới một thương hiệu với thông tin được cung cấp"
    )
    @PostMapping
    public ResponseEntity<SuccessResponse<BrandResponse>> create(
            @Valid @RequestBody CreateBrandRequest request) {
        BrandResponse data = brandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Brand created successfully", data));
    }

    @Operation(
            summary = "Cập nhật thương hiệu",
            description = "Cập nhật thông tin của một thương hiệu dựa trên ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<BrandResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBrandRequest request) {
        BrandResponse data = brandService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Brand updated successfully", data));
    }

    @Operation(
            summary = "Xóa thương hiệu",
            description = "Xóa một thương hiệu dựa trên ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Brand deleted successfully"));
    }
}
