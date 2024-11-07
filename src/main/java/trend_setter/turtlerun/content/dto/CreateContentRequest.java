package trend_setter.turtlerun.content.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import trend_setter.turtlerun.content.entity.Content;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;
import trend_setter.turtlerun.user.entity.User;

/**
 * 컨텐츠 생성 요청 DTO
 *
 * @param title                 컨텐츠 제목
 * @param videoFileId          업로드된 비디오 파일 식별자
 * @param thumbnailFileId      업로드된 썸네일 파일 식별자
 * @param createBlockRequests  설명 블록 목록
 */
public record CreateContentRequest(@NotBlank String title,
                                   @NotNull Long videoFileId,
                                   @NotNull Long thumbnailFileId,
                                   @NotEmpty @Valid List<CreateBlockRequest> createBlockRequests) {
    public CreateContentRequest {
        validateBlockOrder(createBlockRequests);
    }

    private void validateBlockOrder(List<CreateBlockRequest> requests){
        long distinctCount = requests.stream()
            .map(CreateBlockRequest::orderNum).distinct().count();

        if(distinctCount != requests.size()){
            throw new ContentException(ContentErrorCode.DUPLICATE_BLOCK_ORDER);
        }
    }

    public Content toEntity(User user) {
        return new Content(this, user);
    }
}
