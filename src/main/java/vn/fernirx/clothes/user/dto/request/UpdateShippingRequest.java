package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;
import vn.fernirx.clothes.common.annotation.validation.ValidPhone;

import java.io.Serializable;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.UserProfile}
 */
public record UpdateShippingRequest(
        @NullableNotBlank
        @Size(max = 200)
        String shippingName,

        @ValidPhone(allowNull = true)
        String shippingPhone,

        @NullableNotBlank
        @Size(max = 255)
        String shippingStreet,

        @NullableNotBlank
        @Size(max = 100)
        String shippingWard,

        @NullableNotBlank
        @Size(max = 100)
        String shippingDistrict,

        @NullableNotBlank
        @Size(max = 100)
        String shippingProvince
) {}
