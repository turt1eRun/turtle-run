package trend_setter.turtlerun.global.infra.s3.service;

import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.global.error.code.FileErrorCode;
import trend_setter.turtlerun.global.error.exception.FileException;


@Component
public class ImageValidator {

    private static final Set<String> ALLOWED_THUMBNAIL_TYPES = Set.of(
        "image/jpeg",
        "image/png"
    );

    private static final Set<String> ALLOWED_DESCRIPTION_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/gif"
    );

    public void validateThumbnail(MultipartFile file) {
        validateEmpty(file);
        validateThumbnailType(file);
    }

    public void validateDescriptionImage(MultipartFile file) {
        validateEmpty(file);
        validateDescriptionType(file);
    }

    private void validateEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(FileErrorCode.FILE_NOT_FOUND);
        }
    }

    private void validateThumbnailType(MultipartFile file) {
        String type = file.getContentType();
        if (type == null || !ALLOWED_THUMBNAIL_TYPES.contains(type)) {
            throw new FileException(FileErrorCode.INVALID_THUMBNAIL_FORMAT);
        }
    }

    private void validateDescriptionType(MultipartFile file) {
        String type = file.getContentType();
        if (type == null || !ALLOWED_DESCRIPTION_TYPES.contains(type)) {
            throw new FileException(FileErrorCode.INVALID_DESCRIPTION_IMAGE_FORMAT);
        }
    }
}
