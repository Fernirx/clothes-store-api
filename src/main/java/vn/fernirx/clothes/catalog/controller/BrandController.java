package vn.fernirx.clothes.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.catalog.dto.request.BrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.service.BrandService;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<BrandResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<BrandResponse> data = brandService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Brands retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<BrandResponse>> getById(@PathVariable Long id) {
        BrandResponse data = brandService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Brand retrieved successfully", data));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<SuccessResponse<BrandResponse>> getBySlug(@PathVariable String slug) {
        BrandResponse data = brandService.getBySlug(slug);
        return ResponseEntity.ok(SuccessResponse.of("Brand retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<BrandResponse>> create(
            @Valid @RequestBody BrandRequest request) {

        BrandResponse data = brandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Brand created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<BrandResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {

        BrandResponse data = brandService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Brand updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Brand deleted successfully"));
    }
}
