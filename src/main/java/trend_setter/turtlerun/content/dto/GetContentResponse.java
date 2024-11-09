package trend_setter.turtlerun.content.dto;

import java.time.LocalDateTime;
import java.util.List;
import trend_setter.turtlerun.content.entity.Content;

public record GetContentResponse(Long id, String title,
                                 String creator, // dto 객체로 변경필요
                                 GetFileResponse videoFileResponse,
                                 GetFileResponse thumbnailFileResponse,
                                 List<GetDescriptionBlockResponse> descriptionBlockResponses,
                                 long views,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static GetContentResponse from(Content content) {
        return new GetContentResponse(
            content.getId(), content.getTitle(),
            content.getCreator().getNickname(),
            GetFileResponse.from(content.getVideo()),
            GetFileResponse.from(content.getThumbnail()),
            content.getDescriptionBlocks().stream().map(
                GetDescriptionBlockResponse::from).toList(),
            content.getViews(),
            content.getCreatedAt(), content.getUpdatedAt());
    }
}
