package trend_setter.turtlerun.content.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;

/**
 * 설명 블록 생성 요청 DTO
 *
 * @param text       텍스트 블록일 경우 내용
 * @param descFileId 이미지 블록일 경우 파일 식별자
 * @param orderNum   블록 순서
 */
public record CreateBlockRequest(String text, Long descFileId, @NotNull @Positive Integer orderNum) {

    public CreateBlockRequest {
        boolean hasText = text != null && !text.isBlank();
        boolean hasFile = descFileId != null;

        if (hasText && hasFile) {
            throw new ContentException(ContentErrorCode.BLOCK_CONTENT_CONFLICT);
        }
        if (!hasText && !hasFile) {
            throw new ContentException(ContentErrorCode.BLOCK_CONTENT_REQUIRED);
        }
    }
}