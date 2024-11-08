package trend_setter.turtlerun.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trend_setter.turtlerun.user.dto.LoginRequest;
import trend_setter.turtlerun.user.dto.LoginResponse;
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
}
