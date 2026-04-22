package vn.fernirx.clothes.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.order.dto.request.UpdateOrderStatusRequest;
import vn.fernirx.clothes.order.dto.response.OrderResponse;
import vn.fernirx.clothes.order.dto.response.OrderSummaryResponse;
import vn.fernirx.clothes.order.enums.OrderStatus;
import vn.fernirx.clothes.order.service.OrderService;
import vn.fernirx.clothes.security.CustomUserDetails;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Admin Orders API", description = "Các API quản lý đơn hàng")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Lấy danh sách đơn hàng", description = "Hỗ trợ phân trang và lọc theo status")
    public ResponseEntity<SuccessResponse<PageResponse<OrderSummaryResponse>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) OrderStatus status) {
        PageResponse<OrderSummaryResponse> data = orderService.getAllAdmin(page, size, status);
        return ResponseEntity.ok(SuccessResponse.of("Orders retrieved successfully", data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đơn hàng")
    public ResponseEntity<SuccessResponse<OrderResponse>> getById(@PathVariable Long id) {
        OrderResponse data = orderService.getAdminOrderById(id);
        return ResponseEntity.ok(SuccessResponse.of("Order retrieved successfully", data));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái đơn hàng")
    public ResponseEntity<SuccessResponse<OrderResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        OrderResponse data = orderService.updateStatus(id, request, userDetails.getId());
        return ResponseEntity.ok(SuccessResponse.of("Order status updated successfully", data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đơn hàng")
    public ResponseEntity<SuccessResponse<Void>> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(SuccessResponse.of("Order deleted successfully"));
    }
}
