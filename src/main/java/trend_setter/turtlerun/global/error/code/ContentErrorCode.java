package trend_setter.turtlerun.global.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentErrorCode implements ErrorCode {
    TITLE_REQUIRED(400, "C001", "Title is required"),
    DESCRIPTION_BLOCK_REQUIRED(400, "C002", "At least one description block is required"),
    INVALID_BLOCK_ORDER(400, "C003", "Invalid description block order"),
    DUPLICATE_BLOCK_ORDER(400, "C004", "Description block order must not be duplicated"),
    BLOCK_CONTENT_REQUIRED(400, "C005", "Either text or image is required"),
    BLOCK_CONTENT_CONFLICT(400, "C006", "Text and image cannot exist simultaneously"),
    UNAUTHORIZED_CREATOR(403, "C007", "Unauthorized to create content"),
    CONTENT_CREATE_FAILED(500, "C008", "Failed to create content"),
    //videoFile
    VIDEO_FILE_NOT_FOUND(404, "C009", "Video file not found"),
    //thumbnailFile
    THUMBNAIL_FILE_NOT_FOUND(404, "C010", "Thumbnail file not found"),
    //descriptionFile
    DESCRIPTION_FILE_NOT_FOUND(404, "C011", "Description file not found");

    private final int status;
    private final String code;
    private final String message;
}
