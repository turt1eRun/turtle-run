package trend_setter.turtlerun.content.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;

public record ModifyContentRequest(@NotBlank String title,
                                   @NotEmpty @Valid List<BlockRequest>blockRequests) {

    public ModifyContentRequest {
        validateBlockOrder(blockRequests);
    }

    private void validateBlockOrder(List<BlockRequest> requests) {
        long distinctCount = requests.stream()
            .map(BlockRequest::orderNum).distinct().count();

        if (distinctCount != requests.size()) {
            throw new ContentException(ContentErrorCode.DUPLICATE_BLOCK_ORDER);
        }
    }
}
