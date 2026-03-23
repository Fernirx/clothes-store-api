package vn.fernirx.clothes.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.inventory.dto.request.InventoryTransactionRequest;
import vn.fernirx.clothes.inventory.dto.response.InventoryTransactionResponse;
import vn.fernirx.clothes.inventory.service.InventoryTransactionService;

@RestController
@RequestMapping("/api/v1/inventory-transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<InventoryTransactionResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<InventoryTransactionResponse> data =
                transactionService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Inventory transactions retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<InventoryTransactionResponse>> getById(@PathVariable Long id) {
        InventoryTransactionResponse data = transactionService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Inventory transaction retrieved successfully", data));
    }

    @GetMapping("/by-variant/{variantId}")
    public ResponseEntity<SuccessResponse<PageResponse<InventoryTransactionResponse>>> getByVariantId(
            @PathVariable Long variantId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        PageResponse<InventoryTransactionResponse> data =
                transactionService.getByVariantId(variantId, page, size);
        return ResponseEntity.ok(SuccessResponse.of("Inventory transactions for variant retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<InventoryTransactionResponse>> create(
            @Valid @RequestBody InventoryTransactionRequest request) {

        InventoryTransactionResponse data = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Inventory transaction created successfully", data));
    }
}
