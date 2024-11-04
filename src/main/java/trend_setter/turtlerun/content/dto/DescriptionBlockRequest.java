package trend_setter.turtlerun.content.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;

public record DescriptionBlockRequest(String text, MultipartFile file, @NotNull Integer orderNum) {

    public DescriptionBlockRequest {
        boolean hasText = text != null && !text.isBlank();
        boolean hasFile = file != null && !file.isEmpty();

        if (hasText && hasFile) {
            throw new ContentException(ContentErrorCode.BLOCK_CONTENT_CONFLICT);
        }
        if (!hasText && !hasFile) {
            throw new ContentException(ContentErrorCode.BLOCK_CONTENT_REQUIRED);
        }
    }
}