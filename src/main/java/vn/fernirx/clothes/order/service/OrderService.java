package vn.fernirx.clothes.order.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.order.dto.request.CheckoutRequest;
import vn.fernirx.clothes.order.dto.request.UpdateOrderStatusRequest;
import vn.fernirx.clothes.order.dto.response.OrderResponse;
import vn.fernirx.clothes.order.dto.response.OrderSummaryResponse;
import vn.fernirx.clothes.order.enums.OrderStatus;

public interface OrderService {

    OrderResponse checkout(Long userId, CheckoutRequest request);

    OrderResponse getOrderTracking(Long orderId);

    PageResponse<OrderSummaryResponse> getOrderHistory(Long userId, Integer page, Integer size);

    PageResponse<OrderSummaryResponse> getAllAdmin(Integer page, Integer size, OrderStatus status);

    OrderResponse getAdminOrderById(Long id);

    OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request, Long adminId);

    void delete(Long id);
}
