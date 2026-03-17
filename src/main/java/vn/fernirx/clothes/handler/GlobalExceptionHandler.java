package vn.fernirx.clothes.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.AppException;
import vn.fernirx.clothes.common.response.ErrorDetail;
import vn.fernirx.clothes.common.response.ErrorResponse;

import java.util.List;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getCode(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(ex.getCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String field = ex.getName();
        Class<?> requiredType = ex.getRequiredType();
        String message = String.format(
                "must be a %s",
                requiredType != null ? requiredType.getSimpleName() : "valid type"
        );
        ErrorDetail errorDetail = new ErrorDetail(field, message);
        ErrorResponse errorResponse = ErrorResponse.ofValidation(
                ErrorCode.INVALID_PARAMETER,
                "Invalid request parameter",
                List.of(errorDetail)
        );
        return ResponseEntity
                .status(ErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.MALFORMED_JSON,
                "Malformed JSON in request body"
        );
        return ResponseEntity
                .status(ErrorCode.MALFORMED_JSON.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errorDetails = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorDetail(
                        fieldError.getField(),
                        Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Invalid value")
                ))
                .toList();
        ErrorResponse errorResponse = ErrorResponse.ofValidation(
                ErrorCode.VALIDATION_ERROR,
                "One or more fields failed validation",
                errorDetails
        );
        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("[Unhandled exception]", ex);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.of(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        "Internal Server Error"));
    }
}
