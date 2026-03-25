package vn.fernirx.clothes.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.inventory.dto.request.SupplierRequest;
import vn.fernirx.clothes.inventory.dto.response.SupplierResponse;
import vn.fernirx.clothes.inventory.service.SupplierService;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<SupplierResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<SupplierResponse> data = supplierService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Suppliers retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<SupplierResponse>> getById(@PathVariable Long id) {
        SupplierResponse data = supplierService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Supplier retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<SupplierResponse>> create(
            @Valid @RequestBody SupplierRequest request) {

        SupplierResponse data = supplierService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Supplier created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<SupplierResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request) {

        SupplierResponse data = supplierService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Supplier updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Supplier deleted successfully"));
    }
}
