package trend_setter.turtlerun.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trend_setter.turtlerun.user.dto.LoginRequest;
import trend_setter.turtlerun.user.dto.LoginResponse;
import trend_setter.turtlerun.user.dto.RegisterUserRequest;
import trend_setter.turtlerun.user.entity.User;
import trend_setter.turtlerun.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        String encodedPassword = passwordEncoder.encode(registerUserRequest.getPassword());

        User user = RegisterUserRequest.toEntity(registerUserRequest, encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse("로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return new LoginResponse("로그인 성공");
    }
}
