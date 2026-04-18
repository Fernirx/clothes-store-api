package vn.fernirx.clothes.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.cart.dto.response.CartItemResponse;
import vn.fernirx.clothes.cart.dto.response.CartResponse;
import vn.fernirx.clothes.cart.entity.Cart;
import vn.fernirx.clothes.cart.entity.GuestSession;
import vn.fernirx.clothes.cart.mapper.CartItemMapper;
import vn.fernirx.clothes.cart.repository.CartRepository;
import vn.fernirx.clothes.cart.service.CartService;
import vn.fernirx.clothes.cart.service.GuestSessionService;
import vn.fernirx.clothes.user.entity.User;
import vn.fernirx.clothes.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final GuestSessionService guestSessionService;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartResponse getCart(Long userId, String guestToken) {
        Cart cart = getOrCreateCart(userId, guestToken);
        return ;
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

    }
}
