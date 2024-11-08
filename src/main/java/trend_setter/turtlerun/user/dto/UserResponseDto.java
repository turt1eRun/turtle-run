package trend_setter.turtlerun.user.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.user.constant.Role;
import trend_setter.turtlerun.user.entity.User;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;
    private Role role;

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .created_at(user.getCreatedAt())
            .updated_at(user.getUpdatedAt())
            .deleted_at(user.getDeleted_at())
            .role(user.getRole())
            .build();
    }
}
