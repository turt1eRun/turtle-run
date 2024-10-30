package trend_setter.turtlerun.content.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import trend_setter.turtlerun.global.common.StorageDirectory;

@Getter
@RequiredArgsConstructor
public enum ContentDirectory implements StorageDirectory {
    VIDEO("contents/videos"),
    THUMBNAIL("contents/thumbnails"),
    DESCRIPTION("contents/descriptions");

    private final String directory;
}
