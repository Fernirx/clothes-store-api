package vn.fernirx.clothes.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product Variants API", description = "Endpoints for managing product variants")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @GetMapping("/by-product/{productId}")
    @Operation(summary = "Get product variants by product ID", description = "Retrieve a list of product variants associated with a specific product")
    public ResponseEntity<SuccessResponse<List<ProductVariantResponse>>> getByProductId(
            @PathVariable Long productId) {

        List<ProductVariantResponse> data = productVariantService.getByProductId(productId);
        return ResponseEntity.ok(SuccessResponse.of("Product variants retrieved successfully", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product variant by ID", description = "Retrieve a specific product variant by its ID")
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> getById(@PathVariable Long id) {
        ProductVariantResponse data = productVariantService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product variant retrieved successfully", data));
    }

    @PostMapping
    @Operation(summary = "Create a new product variant", description = "Create a new product variant with the provided details")
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> create(
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse data = productVariantService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product variant created successfully", data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product variant", description = "Update the details of an existing product variant by its ID")
    public ResponseEntity<SuccessResponse<ProductVariantResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse data = productVariantService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product variant updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product variant", description = "Delete a specific product variant by its ID")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productVariantService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product variant deleted successfully"));
    }
}
