package trend_setter.turtlerun.content.dto;

import java.time.LocalDateTime;
import java.util.List;
import trend_setter.turtlerun.content.entity.Content;
import trend_setter.turtlerun.user.dto.UserResponseDto;

public record GetContentResponse(Long id, String title,
                                 UserResponseDto creator,
                                 GetFileResponse videoFileResponse,
                                 GetFileResponse thumbnailFileResponse,
                                 List<GetDescriptionBlockResponse> descriptionBlockResponses,
                                 long views,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static GetContentResponse from(Content content) {
        return new GetContentResponse(
            content.getId(), content.getTitle(),
            UserResponseDto.toDto(content.getCreator()),
            GetFileResponse.from(content.getVideo()),
            GetFileResponse.from(content.getThumbnail()),
            content.getDescriptionBlocks().stream().map(
                GetDescriptionBlockResponse::from).toList(),
            content.getViews(),
            content.getCreatedAt(), content.getUpdatedAt());
    }
}
