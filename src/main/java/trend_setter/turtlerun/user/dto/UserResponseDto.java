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
    private Role role;
    private boolean isEmailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int suspensionCount;
    private LocalDateTime suspendedAt;
    private boolean isPermanentlySuspended;

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .role(user.getRole())
            .isEmailVerified(user.isEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .deletedAt(user.getDeletedAt())
            .suspensionCount(user.getSuspensionCount())
            .suspendedAt(user.getSuspendedAt())
            .isPermanentlySuspended(user.isPermanentlySuspended())
            .build();
    }
}
