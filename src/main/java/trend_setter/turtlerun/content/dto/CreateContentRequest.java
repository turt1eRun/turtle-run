package trend_setter.turtlerun.content.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;

public record CreateContentRequest(@NotBlank String title,
                                   @NotNull Long videoFileId,
                                   @NotEmpty @Valid List<DescriptionBlockRequest> descriptionBlockRequests) {
    public CreateContentRequest {
        if(descriptionBlockRequests == null || descriptionBlockRequests.isEmpty()) {
            throw new ContentException(ContentErrorCode.DESCRIPTION_BLOCK_REQUIRED);
        }
        validateBlockOrder(descriptionBlockRequests);
    }

    //순서 중복검사
    private void validateBlockOrder(List<DescriptionBlockRequest> requests){
        long distinctCount = requests.stream()
            .map(DescriptionBlockRequest::orderNum).distinct().count();

        if(distinctCount != requests.size()){
            throw new ContentException(ContentErrorCode.DUPLICATE_BLOCK_ORDER);
        }
    }
}
