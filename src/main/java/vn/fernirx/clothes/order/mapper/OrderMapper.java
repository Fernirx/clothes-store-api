package vn.fernirx.clothes.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.fernirx.clothes.order.dto.response.OrderItemResponse;
import vn.fernirx.clothes.order.dto.response.OrderResponse;
import vn.fernirx.clothes.order.dto.response.OrderStatusHistoryResponse;
import vn.fernirx.clothes.order.dto.response.OrderSummaryResponse;
import vn.fernirx.clothes.order.entity.Order;
import vn.fernirx.clothes.order.entity.OrderItem;
import vn.fernirx.clothes.order.entity.OrderStatusHistory;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "statusHistory", source = "orderStatusHistories")
    OrderResponse toResponse(Order order);

    @Mapping(target = "variantId", source = "variant.id")
    OrderItemResponse toItemResponse(OrderItem orderItem);

    @Mapping(target = "changedById", source = "changedBy.id")
    @Mapping(target = "changedByName", source = "changedBy.email")
    OrderStatusHistoryResponse toHistoryResponse(OrderStatusHistory history);

    OrderSummaryResponse toSummaryResponse(Order order);
}
