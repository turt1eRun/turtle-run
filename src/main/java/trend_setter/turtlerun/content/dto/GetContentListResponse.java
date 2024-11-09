package trend_setter.turtlerun.content.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import trend_setter.turtlerun.content.entity.ThumbnailFile;

public record GetContentListResponse(Long id, String title, String creator, long views, int duration,
                                     GetFileResponse thumbnailFileResponse, LocalDateTime createdAt) {

    @QueryProjection
    public GetContentListResponse(Long id, String title, String creator, long views, int duration,
        ThumbnailFile thumbnailFile, LocalDateTime createdAt) {

        this(id, title, creator, views, duration, GetFileResponse.from(thumbnailFile), createdAt);
    }
}
