package vn.fernirx.clothes.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.cart.dto.request.AddToCartRequest;
import vn.fernirx.clothes.cart.dto.response.CartItemResponse;
import vn.fernirx.clothes.cart.dto.response.CartResponse;
import vn.fernirx.clothes.cart.entity.Cart;
import vn.fernirx.clothes.cart.entity.CartItem;
import vn.fernirx.clothes.cart.entity.GuestSession;
import vn.fernirx.clothes.cart.mapper.CartItemMapper;
import vn.fernirx.clothes.cart.repository.CartItemRepository;
import vn.fernirx.clothes.cart.repository.CartRepository;
import vn.fernirx.clothes.cart.service.CartService;
import vn.fernirx.clothes.cart.service.GuestSessionService;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.catalog.repository.ProductVariantRepository;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final GuestSessionService guestSessionService;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartResponse addToCart(Long userId, String guestToken, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId, guestToken);
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getVariant().getId().equals(request.variantId()))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            cartItemRepository.save(existingItem);
        } else {
            ProductVariant productVariant = productVariantRepository.findById(request.variantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product Variant"));
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .variant(productVariant)
                    .quantity(request.quantity())
                    .build();
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }
        return buildCartResponse(cart);
    }

    @Override
    public CartResponse updateQuantity(Long userId, String guestToken, Long id, Integer quantity) {
        Cart cart = getOrCreateCart(userId, guestToken);
        CartItem item = cartItemRepository.findByIdAndCartId(id, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item"));
        if (quantity <= 0) {
            cart.getCartItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return buildCartResponse(cart);
    }

    @Override
    public CartResponse removeItem(Long userId, String guestToken, Long itemId) {
        Cart cart = getOrCreateCart(userId, guestToken);
        CartItem cartItem = cartItemRepository.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item"));
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        return buildCartResponse(cart);
    }

    @Override
    public CartResponse getCart(Long userId, String guestToken) {
        Cart cart = getOrCreateCart(userId, guestToken);
        return buildCartResponse(cart);
    }

    private Cart getOrCreateCart(Long userId, String guestToken) {
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User"));

            return cartRepository.findByUserId(user.getId())
                    .orElseGet(() -> {
                        Cart cart = new Cart();
                        cart.setUser(user);
                        return cartRepository.save(cart);
                    });
        } else {
            GuestSession session = guestSessionService.getOrCreate(guestToken);
            return cartRepository.findByGuestSession(session)
                    .orElseGet(() -> {
                        Cart cart = new Cart();
                        cart.setGuestSession(session);
                        return cartRepository.save(cart);
                    });
        }
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItemResponse> cartItemResponses = cart.getCartItems().stream()
                .map(cartItemMapper::toResponse)
                .toList();

        BigDecimal totalAmount = cartItemResponses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer total = cartItemResponses.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();
        return CartResponse.builder()
                .cartId(cart.getId())
                .items(cartItemResponses)
                .guestToken(cart.getGuestSession() != null ? cart.getGuestSession().getGuestToken() : null)
                .totalAmount(totalAmount)
                .totalItems(total)
                .build();
    }
}
