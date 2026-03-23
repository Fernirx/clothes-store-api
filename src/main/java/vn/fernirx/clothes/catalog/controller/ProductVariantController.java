package vn.fernirx.clothes.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.ProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;
import vn.fernirx.clothes.catalog.service.ProductVariantService;
import vn.fernirx.clothes.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<SuccessResponse<List<ProductVariantResponse>>> getByProductId(
            @PathVariable Long productId) {

        List<ProductVariantResponse> data = productVariantService.getByProductId(productId);
        return ResponseEntity.ok(SuccessResponse.of("Product variants retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> getById(@PathVariable Long id) {
        ProductVariantResponse data = productVariantService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product variant retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> create(
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse data = productVariantService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product variant created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse data = productVariantService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product variant updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productVariantService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product variant deleted successfully"));
    }
}
