package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.AdminProductFilterRequest;
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
}
