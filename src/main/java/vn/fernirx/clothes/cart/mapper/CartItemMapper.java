package vn.fernirx.clothes.cart.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.cart.dto.response.CartItemResponse;
import vn.fernirx.clothes.cart.entity.CartItem;

import java.math.BigDecimal;
import java.util.Optional;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CartItemMapper {
    @Mapping(target = "variantId", source = "variant.id")
    @Mapping(target = "productName", source = "variant.product.name")
    @Mapping(target = "price", source = ".", qualifiedByName = "toPrice")
    @Mapping(target = "subtotal", source = ".", qualifiedByName = "toSubtotal")
    CartItemResponse toResponse(CartItem cartItem);

    @Named("resolvePrice")
    default BigDecimal resolvePrice(CartItem item) {
        return Optional.ofNullable(item.getVariant().getPrice())
                .orElse(item.getVariant().getProduct().getBasePrice());
    }

    @Named("toPrice")
    default BigDecimal toPrice(CartItem cartItem) {
        if (cartItem == null || cartItem.getVariant() == null) return null;
        return resolvePrice(cartItem);
    }

    @Named("toSubtotal")
    default BigDecimal toSubtotal(CartItem cartItem) {
        if (cartItem == null || cartItem.getVariant() == null) return null;
        return resolvePrice(cartItem).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

}
