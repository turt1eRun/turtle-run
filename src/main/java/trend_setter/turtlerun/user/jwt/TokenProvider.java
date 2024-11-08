package trend_setter.turtlerun.user.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtUtils jwtUtils;

    public String generateAccessToken(Authentication authentication) {
        return createToken(authentication, jwtUtils.getAccessTokenExpirationTime());
    }

    public String generateRefreshToken(Authentication authentication) {
        return createToken(authentication, jwtUtils.getRefreshTokenExpirationTime());
    }

    public String createToken(Authentication authentication, Long expirationTime) {
        return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(authentication, expirationTime))
            .signWith(jwtUtils.createSigningKey(), SignatureAlgorithm.HS256) // 서명
            .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256"); // 서명 알고리즘
        header.put("typ", "JWT"); // 토큰 타입
        return header;
    }

    private Map<String, Object> createClaims(Authentication authentication, Long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "turtle-run"); // 발행자
        claims.put("sub", authentication.getName()); // 사용자명
        claims.put("authorities", getAuthorities(authentication)); // 사용자 권한
        claims.put("iat", new Date()); // 발행 시간
        claims.put("exp", new Date(System.currentTimeMillis() + expirationTime)); // 만료 시간
        claims.put("jti", UUID.randomUUID().toString()); // 고유 토큰 ID
        return claims;
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }
}
