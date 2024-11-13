package trend_setter.turtlerun.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import trend_setter.turtlerun.user.jwt.JwtUtils;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JwtUtils jwtUtils;
    private final JavaMailSender mailSender;

    public String generateEmailToken(String email) {
        Date now = new Date();
        System.out.println(now);

        Date expiration = new Date(System.currentTimeMillis() + jwtUtils.getEmailVerificationTokenExpirationTime());
        System.out.println(expiration);

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(jwtUtils.createSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public void sendVerificationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("터틀런 이메일 인증");
        message.setText("다음 링크를 클릭하여 터틀런 계정 이메일 인증을 완료하세요! http://localhost:8080/api/users/verify-email?token=" + token);
        mailSender.send(message);
    }
}
