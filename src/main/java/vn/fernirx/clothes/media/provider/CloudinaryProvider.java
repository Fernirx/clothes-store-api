package vn.fernirx.clothes.media.provider;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fernirx.clothes.media.exception.MediaException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryProvider implements MediaProvider {
    private final Cloudinary cloudinary;

    @Override
    public Map<?, ?> upload(
            MultipartFile file,
            String folder,
            String publicId) {
        try {
            Map<?, ?> params = ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
            );
            return cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw MediaException.uploadFailed();
        }
    }

    @Override
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw MediaException.deleteFailed();
        }
    }
}
