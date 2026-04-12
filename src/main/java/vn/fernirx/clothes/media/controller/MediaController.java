package vn.fernirx.clothes.media.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.common.response.SuccessResponse;
import vn.fernirx.clothes.media.dto.response.ImageResponse;
import vn.fernirx.clothes.media.enums.MediaContext;
import vn.fernirx.clothes.media.service.MediaService;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
@Tag(name = "Media API", description = "Các API thao tác với lưu trữ media")
public class MediaController {
    private final MediaService mediaService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Tải ảnh lên",
            description = "Tải ảnh lên kho lưu trữ dùng (MultipartFile)"
    )
    public ResponseEntity<SuccessResponse<ImageResponse>> uploadImage(
            @RequestParam MediaContext context,
            @RequestPart("file") MultipartFile file) {
        ImageResponse data = mediaService.uploadImage(file, context.getFolder());
        return ResponseEntity.ok(SuccessResponse.of(
                "Upload image success",
                data
        ));
    }

    @DeleteMapping("/image")
    @Operation(
            summary = "Xóa ảnh",
            description = "Xóa ảnh khỏi kho lưu bằng public id"
    )
    public ResponseEntity<SuccessResponse<Void>> deleteImage(@RequestParam String publicId) {
        mediaService.deleteImage(publicId);
        return ResponseEntity.ok(SuccessResponse.of("Delete image success"));
    }
}
