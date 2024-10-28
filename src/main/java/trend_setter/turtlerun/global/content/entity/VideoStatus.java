package trend_setter.turtlerun.global.content.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoStatus {
    PENDING("대기중"),
    PROCESSING("처리중"),
    READY("준비완료"),
    FAILED("실패");

    private final String description;
}
