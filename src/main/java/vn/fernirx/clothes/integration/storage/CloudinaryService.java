package vn.fernirx.clothes.integration.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public Map<?, ?> uploadImage(
            MultipartFile file,
            String folder,
            String publicId) throws IOException {
        Map<?, ?> params = ObjectUtils.asMap(
                "folder", folder,
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
        return cloudinary.uploader().upload(file.getBytes(), params);
    }

    public Map<String, String> uploadProfileAvatar(Long userId, MultipartFile file) {
        String folder = "clothes/avatars";
        String publicId = "profile_" + userId;
        try {
            Map<?, ?> result = uploadImage(file, folder, publicId);
            return Map.of(
                    "url", (String) result.get("secure_url"),
                    "publicId", (String) result.get("public_id")
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + publicId, e);
        }
    }
}
