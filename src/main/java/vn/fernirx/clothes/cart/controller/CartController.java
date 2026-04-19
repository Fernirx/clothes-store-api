package vn.fernirx.clothes.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @PatchMapping("/items/{id}")
    public ResponseEntity<SuccessResponse<CartResponse>> updateQuantity(
            @PathVariable Long id,
            @Valid @NotNull @Min(0) @RequestParam Integer quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "X-GUEST-TOKEN", required = false) String guestToken) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        CartResponse data = cartService.updateQuantity(userId, guestToken, id, quantity);
        return ResponseEntity.ok(SuccessResponse.of(
                "Item updated successfully",
                data
        ));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<SuccessResponse<CartResponse>> deleteItem(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "X-GUEST-TOKEN", required = false) String guestToken) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        CartResponse data = cartService.removeItem(userId, guestToken, id);
        return ResponseEntity.ok(SuccessResponse.of(
                "Item deleted successfully",
                data
        ));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<Void>> clearCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "X-GUEST-TOKEN", required = false) String guestToken) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        cartService.clearCart(userId, guestToken);
        return ResponseEntity.ok(SuccessResponse.of(
                "Cart cleared successfully"
        ));
    }

    @PostMapping("/merge")
    public ResponseEntity<SuccessResponse<CartResponse>> mergeCartAfterLogin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "X-GUEST-TOKEN", required = false) String guestToken) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        cartService.mergeCart(userId, guestToken);
        CartResponse response = cartService.getCart(userId, null);
        return ResponseEntity.ok(SuccessResponse.of(
                "Cart merge successfully",
                response
        ));
    }
}
