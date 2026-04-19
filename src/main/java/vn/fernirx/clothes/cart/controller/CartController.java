package vn.fernirx.clothes.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.fernirx.clothes.cart.dto.request.AddToCartRequest;
import vn.fernirx.clothes.cart.dto.response.CartResponse;
import vn.fernirx.clothes.cart.service.CartService;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.security.CustomUserDetails;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<SuccessResponse<CartResponse>> getCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "X-GUEST-TOKEN", required = false) String guestToken) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        CartResponse data = cartService.getCart(userId, guestToken);
        return ResponseEntity.ok(SuccessResponse.of(
                "Cart retrieved successfully",
                data
        ));
    }

    @PostMapping("/items")
    public ResponseEntity<SuccessResponse<CartResponse>> addItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "X-GUEST-TOKEN", required = false) String guestToken,
            @Valid @RequestBody AddToCartRequest request) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        CartResponse data = cartService.addToCart(userId, guestToken, request);
        return ResponseEntity.ok(SuccessResponse.of(
                "Item added successfully",
                data
        ));
    }
}
