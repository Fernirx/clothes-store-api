package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.ProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductSummaryResponse;
import vn.fernirx.clothes.catalog.service.ProductService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách sản phẩm",
            description = "Lấy danh sách tất cả sản phẩm hoạt động với phân trang và sắp xếp"
    )
    public ResponseEntity<SuccessResponse<PageResponse<ProductSummaryResponse>>> getAllByActiveTrue(
            @ParameterObject @ModelAttribute ProductFilterRequest filter,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<ProductSummaryResponse> data = productService.getAllByActiveTrue(page, size, sortBy, sortDir, filter);
        return ResponseEntity.ok(SuccessResponse.of("Products retrieved successfully", data));
    }

    @Operation(
            summary = "Lấy product theo slug",
            description = "Lấy thông tin chi tiết của một product dựa trên slug"
    )
    @GetMapping("/{slug}")
    public ResponseEntity<SuccessResponse<ProductDetailResponse>> getBySlug(@PathVariable String slug) {
        ProductDetailResponse data = productService.getDetailBySlug(slug);
        return ResponseEntity.ok(SuccessResponse.of("Product retrieved successfully", data));
    }
}
