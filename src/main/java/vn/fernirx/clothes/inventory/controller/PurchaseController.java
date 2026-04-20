package vn.fernirx.clothes.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/admin/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchase Management", description = "Các API để quản lý các giao dịch mua hàng, bao gồm tạo, cập nhật, xóa và truy vấn thông tin mua hàng.")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    @Operation( summary = "Lấy danh sách các giao dịch mua hàng", description = "Trả về danh sách các giao dịch mua hàng với khả năng phân trang và sắp xếp.")
    public ResponseEntity<SuccessResponse<PageResponse<PurchaseResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        PageResponse<PurchaseResponse> data = purchaseService.getAll(page, size, sortBy, sortDir);
        return ResponseEntity.ok(SuccessResponse.of("Purchases retrieved successfully", data));
    }

    @GetMapping("/{id}")
    @Operation( summary = "Lấy thông tin chi tiết của một giao dịch mua hàng", description = "Trả về thông tin chi tiết của một giao dịch mua hàng dựa trên ID.")
    public ResponseEntity<SuccessResponse<PurchaseResponse>> getById(@PathVariable Long id) {
        PurchaseResponse data = purchaseService.getById(id);
        return ResponseEntity.ok(SuccessResponse.of("Purchase retrieved successfully", data));
    }

    @PostMapping
    @Operation (summary = "Tạo mới một giao dịch mua hàng", description = "Tạo mới một giao dịch mua hàng với thông tin chi tiết về sản phẩm, số lượng, nhà cung cấp, v.v.")
    public ResponseEntity<SuccessResponse<PurchaseResponse>> create(
            @Valid @RequestBody PurchaseRequest request) {

        PurchaseResponse data = purchaseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Purchase created successfully", data));
    }

    @PutMapping("/{id}")
    @Operation (summary = "Cập nhật thông tin của một giao dịch mua hàng", description = "Cập nhật thông tin chi tiết của một giao dịch mua hàng dựa trên ID, bao gồm các trường như sản phẩm, số lượng, nhà cung cấp, v.v.")
    public ResponseEntity<SuccessResponse<PurchaseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequest request) {

        PurchaseResponse data = purchaseService.update(id, request);
        return ResponseEntity.ok(SuccessResponse.of("Purchase updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation (summary = "Xóa một giao dịch mua hàng", description = "Xóa một giao dịch mua hàng dựa trên ID.")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        purchaseService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Purchase deleted successfully"));
    }
}
