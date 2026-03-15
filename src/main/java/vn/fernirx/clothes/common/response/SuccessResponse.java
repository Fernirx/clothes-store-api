package vn.fernirx.clothes.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(
        String message,
        T data,
        Instant timestamp) {

    public static <T> SuccessResponse<T> of(String message, T data) {
        return new SuccessResponse<>(message, data, Instant.now());
    }

    public static <Void> SuccessResponse<Void> of(String message) {
        return new SuccessResponse<>(message, null, Instant.now());
    }
}