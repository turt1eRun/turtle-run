package trend_setter.turtlerun.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trend_setter.turtlerun.user.dto.LoginRequest;
import trend_setter.turtlerun.user.dto.LoginResponse;
import trend_setter.turtlerun.user.dto.RegisterUserRequest;
import trend_setter.turtlerun.user.entity.User;
import trend_setter.turtlerun.user.jwt.TokenProvider;
import trend_setter.turtlerun.user.jwt.TokenValidator;
import trend_setter.turtlerun.user.repository.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    private final TokenValidator tokenValidator;

    @Transactional
    public void registerUser(RegisterUserRequest registerUserRequest) {
        String encodedPassword = passwordEncoder.encode(registerUserRequest.getPassword());
        User user = RegisterUserRequest.toEntity(registerUserRequest, encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        setTokensInResponse(httpServletResponse, accessToken, refreshToken);

        return new LoginResponse("로그인 성공");
    }

    private Authentication authenticateUser(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            return authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void setTokensInResponse(HttpServletResponse httpServletResponse, String accessToken, String refreshToken) {
        httpServletResponse.setHeader("Authorization", "Bearer " + accessToken);
        cookieService.addRefreshTokenCookie(httpServletResponse, refreshToken);
    }

    public void logout(HttpServletResponse httpServletResponse) {
        cookieService.removeRefreshTokenCookie(httpServletResponse);

        httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Expires", "0");
    }
}
