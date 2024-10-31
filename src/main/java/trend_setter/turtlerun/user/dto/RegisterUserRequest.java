package trend_setter.turtlerun.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import trend_setter.turtlerun.user.constant.Role;
import trend_setter.turtlerun.user.entity.User;

@Getter
@Builder
public class RegisterUserRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    public static User toEntity(RegisterUserRequest request, String encodedPassword) {
    return User.builder()
        .email(request.email)
        .password(encodedPassword)
        .nickname(request.nickname)
        .role(Role.TURTLE)
        .build();
    }
}
