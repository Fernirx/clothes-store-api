package vn.fernirx.clothes.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.cart.entity.Cart;
import vn.fernirx.clothes.cart.entity.CartItem;
import vn.fernirx.clothes.cart.repository.CartItemRepository;
import vn.fernirx.clothes.cart.repository.CartRepository;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;
import vn.fernirx.clothes.order.dto.request.CheckoutRequest;
import vn.fernirx.clothes.order.dto.request.UpdateOrderStatusRequest;
import vn.fernirx.clothes.order.dto.response.OrderResponse;
import vn.fernirx.clothes.order.dto.response.OrderSummaryResponse;
import vn.fernirx.clothes.order.entity.Order;
import vn.fernirx.clothes.order.entity.OrderItem;
import vn.fernirx.clothes.order.entity.OrderStatusHistory;
import vn.fernirx.clothes.order.enums.OrderStatus;
import vn.fernirx.clothes.order.enums.PaymentStatus;
import vn.fernirx.clothes.order.mapper.OrderMapper;
import vn.fernirx.clothes.order.repository.OrderItemRepository;
import vn.fernirx.clothes.order.repository.OrderRepository;
import vn.fernirx.clothes.order.repository.OrderStatusHistoryRepository;
import vn.fernirx.clothes.order.service.OrderService;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart"));

        List<CartItem> selectedItems = cartItemRepository.findAllWithVariantByIdInAndCartId(
                request.selectedCartItemIds(), cart.getId());

        if (selectedItems.size() != request.selectedCartItemIds().size()) {
            throw new IllegalArgumentException("Một hoặc nhiều sản phẩm không tồn tại trong giỏ hàng");
        }

        for (CartItem item : selectedItems) {
            ProductVariant variant = item.getVariant();
            if (variant.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException(
                        "Sản phẩm '" + variant.getProduct().getName() + "' không đủ tồn kho");
            }
        }

        BigDecimal subtotal = selectedItems.stream()
                .map(item -> resolvePrice(item.getVariant()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        Order order = Order.builder()
                .user(user)
                .code(generateOrderCode())
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.UNPAID)
                .paymentMethod(request.paymentMethod())
                .recipientName(request.recipientName())
                .recipientPhone(request.recipientPhone())
                .shippingStreet(request.shippingStreet())
                .shippingWard(request.shippingWard())
                .shippingDistrict(request.shippingDistrict())
                .shippingProvince(request.shippingProvince())
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .couponCode(request.couponCode())
                .note(request.note())
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();

        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : selectedItems) {
            ProductVariant variant = cartItem.getVariant();
            BigDecimal unitPrice = resolvePrice(variant);
            BigDecimal itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .variant(variant)
                    .productCode(variant.getProduct().getCode())
                    .productName(variant.getProduct().getName())
                    .variantSku(variant.getSku())
                    .variantSize(variant.getSize())
                    .variantColor(variant.getColor())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .discountAmount(BigDecimal.ZERO)
                    .subtotal(itemSubtotal)
                    .build();

            orderItems.add(orderItem);
            variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
        }

        orderItemRepository.saveAll(orderItems);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .oldStatus(null)
                .newStatus(OrderStatus.PENDING)
                .build();
        orderStatusHistoryRepository.save(history);

        cartItemRepository.deleteAll(selectedItems);

        return orderMapper.toResponse(orderRepository.findByIdWithDetails(order.getId()).orElseThrow());
    }

    @Override
    public OrderResponse getOrderTracking(Long orderId) {
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));
        return orderMapper.toResponse(order);
    }

    @Override
    public PageResponse<OrderSummaryResponse> getOrderHistory(Long userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User");
        }
        Pageable pageable = PaginationUtil.createPageable(page, size, "createdAt", "desc");
        Page<OrderSummaryResponse> result = orderRepository.findByUserId(userId, pageable)
                .map(orderMapper::toSummaryResponse);
        return PageResponse.of(result);
    }

    @Override
    public PageResponse<OrderSummaryResponse> getAllAdmin(Integer page, Integer size, OrderStatus status) {
        Pageable pageable = PaginationUtil.createPageable(page, size, "createdAt", "desc");
        Page<OrderSummaryResponse> result = (status != null
                ? orderRepository.findByStatus(status, pageable)
                : orderRepository.findAll(pageable))
                .map(orderMapper::toSummaryResponse);
        return PageResponse.of(result);
    }

    @Override
    public OrderResponse getAdminOrderById(Long id) {
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request, Long adminId) {
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(request.status());

        if (request.adminNote() != null) {
            order.setAdminNote(request.adminNote());
        }

        if (request.status() == OrderStatus.CONFIRMED && order.getConfirmedAt() == null) {
            order.setConfirmedAt(LocalDateTime.now());
        }

        if (request.status() == OrderStatus.DELIVERED && order.getDeliveredAt() == null) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        orderRepository.save(order);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .changedBy(admin)
                .oldStatus(oldStatus)
                .newStatus(request.status())
                .build();
        orderStatusHistoryRepository.save(history);

        return orderMapper.toResponse(orderRepository.findByIdWithDetails(id).orElseThrow());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order");
        }
        orderRepository.deleteById(id);
    }

    private BigDecimal resolvePrice(ProductVariant variant) {
        return variant.getPrice() != null ? variant.getPrice() : variant.getProduct().getBasePrice();
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
