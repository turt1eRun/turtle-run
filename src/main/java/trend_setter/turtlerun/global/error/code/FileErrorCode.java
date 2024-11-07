package trend_setter.turtlerun.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {
    FILE_UPLOAD_ERROR(500, "F001", "failed to upload file"),
    FILE_NOT_FOUND(400, "F002", "file not found"),
    INVALID_VIDEO_FORMAT(400, "F003", "invalid file type"),
    FILE_SIZE_EXCEED(400, "F004", "exceed file size limit"),
    VIDEO_DURATION_EXTRACT_ERROR(400, "F005", "failed to extract video duration"),
    FAILED_TO_ABORT(400, "F006", "failed to abort"),
    INVALID_THUMBNAIL_FORMAT(400, "F007", "invalid thumbnail format"),
    INVALID_DESCRIPTION_IMAGE_FORMAT(400, "F008", "invalid description image format");
    private final int status;
    private final String code;
    private final String message;
}