package trend_setter.turtlerun.content.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoResolution {
    HD_540P("540p", 540),
    HD_720P("720p", 720),
    HD_1080P("1080p", 1080);

    private final String label;
    private final int height;
}
