package trend_setter.turtlerun.global.infra.s3.service;

import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.global.error.code.FileErrorCode;
import trend_setter.turtlerun.global.error.exception.FileException;

@Component
public class VideoValidator {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 1024;

    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
        "video/mp4",
        "video/mpeg"
    );

    public void validate(MultipartFile file) {
        validateEmpty(file);
        validateSize(file);
        validateType(file);
    }

    private void validateEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(FileErrorCode.FILE_NOT_FOUND);
        }
    }

    private void validateSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileException(FileErrorCode.FILE_SIZE_EXCEED);
        }
    }

    private void validateType(MultipartFile file) {
        String type = file.getContentType();
        if (type == null || !ALLOWED_VIDEO_TYPES.contains(type)) {
            throw new FileException(FileErrorCode.INVALID_VIDEO_FORMAT);
        }
    }
}