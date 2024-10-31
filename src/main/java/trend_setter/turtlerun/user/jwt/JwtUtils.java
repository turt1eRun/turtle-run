/*
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
    @Value("${jwt.expiration-time}")
    private Long expirationTime;

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Base64 디코딩
        return Keys.hmacShaKeyFor(keyBytes); // HMAC SHA 키 생성
    }
}
*/