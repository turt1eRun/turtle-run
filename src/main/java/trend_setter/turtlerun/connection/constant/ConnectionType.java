package trend_setter.turtlerun.connection.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConnectionType {
    QUESTION("질문형"),
    NORMAL("일반형");

    private final String description;
}
