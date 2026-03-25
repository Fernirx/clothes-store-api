package vn.fernirx.clothes.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.inventory.dto.request.PurchaseRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseResponse;
import vn.fernirx.clothes.inventory.service.PurchaseService;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<PurchaseResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<PurchaseResponse> data = purchaseService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Purchases retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<PurchaseResponse>> getById(@PathVariable Long id) {
        PurchaseResponse data = purchaseService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Purchase retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<PurchaseResponse>> create(
            @Valid @RequestBody PurchaseRequest request) {

        PurchaseResponse data = purchaseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Purchase created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<PurchaseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequest request) {

        PurchaseResponse data = purchaseService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Purchase updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        purchaseService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Purchase deleted successfully"));
    }
}
