package vn.fernirx.clothes.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.order.dto.request.CheckoutRequest;
import vn.fernirx.clothes.order.dto.response.OrderResponse;
import vn.fernirx.clothes.order.dto.response.OrderSummaryResponse;
import vn.fernirx.clothes.order.service.OrderService;
import vn.fernirx.clothes.security.CustomUserDetails;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders API", description = "Các API đơn hàng")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout-selected")
    @Operation(summary = "Tạo đơn hàng từ các sản phẩm được chọn trong giỏ")
    public ResponseEntity<SuccessResponse<OrderResponse>> checkoutSelected(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CheckoutRequest request) {
        OrderResponse data = orderService.checkout(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of("Order created successfully", data));
    }

    @GetMapping("/tracking/{orderId}")
    @Operation(summary = "Lấy chi tiết đơn hàng")
    public ResponseEntity<SuccessResponse<OrderResponse>> trackOrder(@PathVariable Long orderId) {
        OrderResponse data = orderService.getOrderTracking(orderId);
        return ResponseEntity.ok(SuccessResponse.of("Order retrieved successfully", data));
    }

    @GetMapping("/history")
    @Operation(summary = "Lấy lịch sử đơn hàng theo userId")
    public ResponseEntity<SuccessResponse<PageResponse<OrderSummaryResponse>>> getOrderHistory(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        PageResponse<OrderSummaryResponse> data = orderService.getOrderHistory(userId, page, size);
        return ResponseEntity.ok(SuccessResponse.of("Order history retrieved successfully", data));
    }
}
