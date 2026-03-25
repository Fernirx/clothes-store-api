package vn.fernirx.clothes.catalog.controller;

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
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<SuccessResponse<List<ProductImageResponse>>> getByProductId(
            @PathVariable Long productId) {

        List<ProductImageResponse> data = productImageService.getByProductId(productId);
        return ResponseEntity.ok(SuccessResponse.of("Product images retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductImageResponse>> getById(@PathVariable Long id) {
        ProductImageResponse data = productImageService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product image retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ProductImageResponse>> create(
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse data = productImageService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product image created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductImageResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse data = productImageService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product image updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productImageService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product image deleted successfully"));
    }
}
