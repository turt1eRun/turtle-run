package trend_setter.turtlerun.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String message; // 로그인 성공/실패 메시지
}
