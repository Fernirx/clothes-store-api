package vn.fernirx.clothes.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.fernirx.clothes.common.enums.ErrorCode;

import java.time.Instant;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message,
        ErrorCode code,
        Set<ErrorDetail> fields,
        Instant timestamp) {

    public static ErrorResponse of(String message, ErrorCode code) {
        return new ErrorResponse(message, code, null, Instant.now());
    }

    public static ErrorResponse ofValidation(String message, ErrorCode code,
                                             Set<ErrorDetail> fields) {
        return new ErrorResponse(message, code, fields, Instant.now());
    }
}