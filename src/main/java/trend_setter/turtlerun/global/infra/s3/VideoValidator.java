package trend_setter.turtlerun.global.infra.s3;

import jakarta.validation.ValidationException;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class VideoValidator {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 1024;

    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
        "video/mp4",
        "video/quicktime",
        "video/x-msvideo",
        "video/mpeg",
        "video/webm"
    );

    public void validate(MultipartFile file) {
        validateEmpty(file);
        validateSize(file);
        validateType(file);
    }

    private void validateEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("File is empty");
        }
    }

    private void validateSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException("File is too large");
        }
    }

    private void validateType(MultipartFile file) {
        String type = file.getContentType();
        if (type == null || !ALLOWED_VIDEO_TYPES.contains(type)) {
            throw new ValidationException("File type not supported");
        }
    }
}
