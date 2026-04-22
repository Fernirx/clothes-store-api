package vn.fernirx.clothes.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.order.enums.PaymentMethod;

import java.util.List;

public record CheckoutRequest(
        @NotEmpty(message = "Phải chọn ít nhất 1 sản phẩm")
        List<Long> selectedCartItemIds,

        @NotNull(message = "Phương thức thanh toán không được để trống")
        PaymentMethod paymentMethod,

        @NotBlank(message = "Tên người nhận không được để trống")
        @Size(max = 200)
        String recipientName,

        @NotBlank(message = "Số điện thoại người nhận không được để trống")
        @Size(max = 15)
        String recipientPhone,

        @NotBlank(message = "Địa chỉ giao hàng không được để trống")
        String shippingStreet,

        @NotBlank(message = "Phường/Xã không được để trống")
        @Size(max = 100)
        String shippingWard,

        @NotBlank(message = "Quận/Huyện không được để trống")
        @Size(max = 100)
        String shippingDistrict,

        @NotBlank(message = "Tỉnh/Thành phố không được để trống")
        @Size(max = 100)
        String shippingProvince,

        @Size(max = 50)
        String couponCode,

        String note
) {
}