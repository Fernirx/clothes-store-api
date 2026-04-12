package vn.fernirx.clothes.media.exception;

import lombok.Getter;
import vn.fernirx.clothes.common.enums.ErrorCode;
import vn.fernirx.clothes.common.exception.AppException;

@Getter
public class MediaException extends AppException {
    public MediaException(String message) {
        super(ErrorCode.MEDIA_ERROR, message);
    }

    public static MediaException uploadFailed() {
        return new MediaException("Failed to upload media");
    }

    public static MediaException deleteFailed() {
        return new MediaException("Failed to delete media");
    }
}
