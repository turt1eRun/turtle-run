package trend_setter.turtlerun.global.content.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockType {
    TEXT("텍스트"),
    IMAGE("이미지");

    private final String description;
}
