package trend_setter.turtlerun.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import trend_setter.turtlerun.user.entity.User;
import trend_setter.turtlerun.user.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public User getUserFromToken(String token) {
        String email = extractClaimsFromToken(token).getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(null);
        return user;
    }

    public String extractTokenFromHeader(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String extractedToken = bearerToken.substring(7);

            return extractedToken;
        }

        return null;
    }

    public boolean validateToken(String token) {
        Claims claims = extractClaimsFromToken(token);

        // 현재 시간이 만료 시간 이후가 아니라면 true를 반환합니다.
        return !claims.getExpiration().before(new Date());
    }

    public Claims extractClaimsFromToken(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
            .setSigningKey(jwtUtils.createSigningKey())
            .build()
            .parseClaimsJws(token);

        Claims claims = jws.getBody();

        return claims;
    }

    public UserDetails extractUserDetailsFromToken(String token) {
        String username = extractClaimsFromToken(token).getSubject();

        return userDetailsService.loadUserByUsername(username);
    }

    // 4
    public String extractAccessTokenFromResponseHeader(HttpServletResponse httpServletResponse) {
        String authorizationHeader = httpServletResponse.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    public User validateTokenAndGetUser(String token) {
        if (validateToken(token)) {
            return getUserFromToken(token);
        }
        throw new UsernameNotFoundException("no");
    }
}
