package trend_setter.turtlerun.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trend_setter.turtlerun.user.dto.ChangeNicknameRequest;
import trend_setter.turtlerun.user.dto.ChangePasswordRequest;
import trend_setter.turtlerun.user.dto.LoginRequest;
import trend_setter.turtlerun.user.dto.LoginResponse;
import trend_setter.turtlerun.user.dto.MemberEmailVerifiedResponse;
import trend_setter.turtlerun.user.dto.RegisterUserRequest;
import trend_setter.turtlerun.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public void registerUser (@RequestBody RegisterUserRequest registerUserRequest) {
        userService.registerUser(registerUserRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse Loginresponse = userService.login(loginRequest, response);
        return ResponseEntity.ok(Loginresponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse httpServletResponse) {
        userService.logout(httpServletResponse);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/verify-email")
    public MemberEmailVerifiedResponse verifyEmail(@RequestParam String token) {
        MemberEmailVerifiedResponse memberEmailVerifiedResponse = userService.verifyEmail(token);
        return memberEmailVerifiedResponse;
    }

    @PutMapping("/change-nickname")
    public ResponseEntity<String> changeNickname(HttpServletRequest httpServletRequest, @RequestBody ChangeNicknameRequest changeNicknameRequest) {
        userService.changeNickname(httpServletRequest, changeNicknameRequest);
        return ResponseEntity.ok("닉네임 변경 성공");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(HttpServletRequest httpServletRequest, @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(httpServletRequest, changePasswordRequest);
        return ResponseEntity.ok("비밀번호 변경 성공");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteAccount() {
        userService.deleteAccount();
        return ResponseEntity.ok("회원 탈퇴 신청이 완료되었습니다.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelAccount() {
        userService.cancelDeleteAccount();
        return ResponseEntity.ok("회원 탈퇴가 취소되었습니다.");
    }
}
