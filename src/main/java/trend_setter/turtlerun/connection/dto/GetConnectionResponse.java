package trend_setter.turtlerun.connection.dto;

import java.time.LocalDateTime;
import java.util.List;
import trend_setter.turtlerun.connection.constant.ConnectionType;
import trend_setter.turtlerun.connection.entity.Connection;

public record GetConnectionResponse(
    Long id,
    Long parentId,
    List<GetConnectionResponse> replies,
    String writerName,
    ConnectionType connectionType,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt) {

    public static GetConnectionResponse from(Connection connection) {
        return new GetConnectionResponse(
            connection.getId(),
            connection.getParent().getId(),
            connection.getReplies().stream().map(GetConnectionResponse::from).toList(),
            connection.getWriter().getNickname(),
            connection.getConnectionType(),
            connection.getContent(),
            connection.getCreatedAt(), connection.getUpdatedAt());
    }
}
