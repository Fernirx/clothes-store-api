package vn.fernirx.clothes.inventory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentResponse;
import vn.fernirx.clothes.inventory.service.StockAdjustmentService;

@RestController
@RequestMapping("/api/v1/stock-adjustments")
@RequiredArgsConstructor
public class StockAdjustmentController {

    private final StockAdjustmentService stockAdjustmentService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<StockAdjustmentResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<StockAdjustmentResponse> data =
                stockAdjustmentService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Stock adjustments retrieved successfully", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<StockAdjustmentResponse>> getById(@PathVariable Long id) {
        StockAdjustmentResponse data = stockAdjustmentService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Stock adjustment retrieved successfully", data));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<StockAdjustmentResponse>> create(
            @Valid @RequestBody StockAdjustmentRequest request) {

        StockAdjustmentResponse data = stockAdjustmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Stock adjustment created successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<StockAdjustmentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequest request) {

        StockAdjustmentResponse data = stockAdjustmentService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Stock adjustment updated successfully", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        stockAdjustmentService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Stock adjustment deleted successfully"));
    }
}
