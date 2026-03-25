package vn.fernirx.clothes.catalog.controller;

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
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<ProductResponse> data = productService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Products retrieved successfully", data));
    }

    @GetMapping("/active")
    public ResponseEntity<SuccessResponse<PageResponse<ProductResponse>>> getActive(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<ProductResponse> data = productService.getActive(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Active products retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductResponse>> getById(@PathVariable Long id) {
        ProductResponse data = productService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Product retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<ProductResponse>> create(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse data = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Product created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse data = productService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Product updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Product deleted successfully"));
    }
}
