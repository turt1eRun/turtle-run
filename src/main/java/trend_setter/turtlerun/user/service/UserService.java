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
import trend_setter.turtlerun.user.dto.ChangeNicknameRequest;
import trend_setter.turtlerun.user.dto.ChangePasswordRequest;
import trend_setter.turtlerun.user.dto.LoginRequest;
import trend_setter.turtlerun.user.dto.LoginResponse;
import trend_setter.turtlerun.user.dto.MemberEmailVerifiedResponse;
import trend_setter.turtlerun.user.dto.RegisterUserRequest;
import trend_setter.turtlerun.user.dto.UserResponseDto;
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
    private final MailService mailService;

    @Transactional
    public UserResponseDto registerUser(RegisterUserRequest registerUserRequest) {
        String encodedPassword = passwordEncoder.encode(registerUserRequest.getPassword());
        User user = RegisterUserRequest.toEntity(registerUserRequest, encodedPassword);
        User saveUser = userRepository.save(user);

        String token = mailService.generateEmailToken(user.getEmail());
        mailService.sendVerificationEmail(user.getEmail(), token);

        return UserResponseDto.toDto(saveUser);
    }

    @Transactional
    public MemberEmailVerifiedResponse verifyEmail(String token) {
        String email = tokenValidator.extractUsernameFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow();

        if(user.isEmailVerified()) {
            throw new BadCredentialsException("Email already verified");
        }

        user.emailVerificationComplete();
        userRepository.save(user);
        return MemberEmailVerifiedResponse.builder().email(user.getEmail()).build();
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

    private User getAuthenticatedMember(HttpServletRequest request) {
        String token = tokenValidator.extractTokenFromHeader(request);
        return tokenValidator.validateTokenAndGetUser(token);
    }

    @Transactional
    public void changeNickname(HttpServletRequest httpServletRequest, ChangeNicknameRequest changeNicknameRequest) {
        User user = getAuthenticatedMember(httpServletRequest);

        String nickname = changeNicknameRequest.getNickname();

        boolean exist = userRepository.existsByNickname(nickname);

        if(exist) {
            throw new BadCredentialsException("이미 존재하는 닉네임입니다.");
        }

        if(user.getNickname().equals(nickname)) {
            throw new BadCredentialsException("기존 닉네임과 동일합니다.");
        }

        user.changeNickname(nickname);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(HttpServletRequest httpServletRequest, ChangePasswordRequest changePasswordRequest) {
        User user = getAuthenticatedMember(httpServletRequest);

        String nowPassword = changePasswordRequest.getNowPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        if(!passwordEncoder.matches(nowPassword, user.getPassword())) {
            throw new BadCredentialsException("현재 비밀번호가 틀렸습니다.");
        }

        if(passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadCredentialsException("현재 비밀번호와 동일합니다.");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        user.deleteAccount();
        userRepository.save(user);
    }

    @Transactional
    public void cancelDeleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        user.cancelDeleteAccount();
        userRepository.save(user);
    }
}
