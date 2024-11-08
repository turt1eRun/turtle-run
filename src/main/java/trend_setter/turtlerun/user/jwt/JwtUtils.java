package trend_setter.turtlerun.user.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token.expiration-time}")
    private Long accessTokenExpirationTime;

    @Getter
    @Value("${jwt.refresh-token.expiration-time}")
    private Long refreshTokenExpirationTime;

    public Key createSigningKey() {
        byte[] decodedKeyBytes = Decoders.BASE64.decode(secretKey); // Base64로 인코딩된 비밀키를 바이트 배열로 디코딩한다.
        Key signingKey = Keys.hmacShaKeyFor(decodedKeyBytes); // 디코딩된 바이트 배열을 사용하여 HMAC 서명 키를 생성한다.

        return signingKey; // JWT 서명을 위한 키 반환
    }
}
