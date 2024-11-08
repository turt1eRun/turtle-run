/*
package trend_setter.turtlerun.user.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtUtils jwtUtils;

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
            .setIssuer("turtle-run") // JWT의 발행자 설정. 이 값은 클라이언트가 토큰의 출처를 알 수 있게 합니다.
            .setClaims(claims)
            .setSubject(email) // JWT를 소유하는 사용자에 대한 식별 정보
            .setIssuedAt(new Date(System.currentTimeMillis())) // JWT 발행 시간
            .setExpiration(new Date(System.currentTimeMillis() + jwtUtils.getExpirationTime())) // JWT 만료 시간
            .signWith(jwtUtils.getSigningKey()) // JWT 서명
            .compact();
    }
}
 */