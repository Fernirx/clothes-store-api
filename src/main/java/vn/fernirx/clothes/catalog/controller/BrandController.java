package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.service.BrandService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Tag(name = "Brand API", description = "Các endpoint liên quan đến thương hiệu sản phẩm")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách thương hiệu",
            description = "Lấy danh sách tất cả thương hiệu hoạt động với phân trang và sắp xếp"
    )
    public ResponseEntity<SuccessResponse<PageResponse<BrandResponse>>> getAllByActiveTrue(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<BrandResponse> data = brandService.getAllByActiveTrue(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Brands retrieved successfully", data));
    }

    @Operation(
            summary = "Lấy thương hiệu theo slug",
            description = "Lấy thông tin chi tiết của một thương hiệu dựa trên slug"
    )
    @GetMapping("/{slug}")
    public ResponseEntity<SuccessResponse<BrandResponse>> getBySlug(@PathVariable String slug) {
        BrandResponse data = brandService.getBySlug(slug);
        return ResponseEntity.ok(SuccessResponse.of("Brand retrieved successfully", data));
    }
}
