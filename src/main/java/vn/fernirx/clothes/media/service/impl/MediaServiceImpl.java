package vn.fernirx.clothes.media.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.media.dto.response.ImageResponse;
import vn.fernirx.clothes.media.provider.MediaProvider;
import vn.fernirx.clothes.media.service.MediaService;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaProvider provider;

    @Override
    public ImageResponse uploadImage(MultipartFile file, String folder) {
        String publicId = UUID.randomUUID().toString();
        Map<?, ?> result = provider.upload(file, folder, publicId);
        return new ImageResponse(
                (String) result.get("secure_url"),
                (String) result.get("public_id")
        );
    }

    @Override
    public void deleteImage(String publicId) {
        provider.delete(publicId);
    }
}
