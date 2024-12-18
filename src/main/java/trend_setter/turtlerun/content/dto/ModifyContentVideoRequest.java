package trend_setter.turtlerun.content.dto;

import jakarta.validation.constraints.NotNull;

public record ModifyContentVideoRequest(@NotNull Long videoFileId) {

}
