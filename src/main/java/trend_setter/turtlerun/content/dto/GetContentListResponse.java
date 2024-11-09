package trend_setter.turtlerun.content.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record GetContentListResponse(Long id, String title, String creator, long views,
                                     int duration, LocalDateTime createdAt) {

    @QueryProjection
    public GetContentListResponse(Long id, String title, String creator, long views, int duration,
        LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.views = views;
        this.duration = duration;
        this.createdAt = createdAt;
    }
}
