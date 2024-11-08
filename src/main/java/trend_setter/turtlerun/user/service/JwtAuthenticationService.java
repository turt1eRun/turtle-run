package trend_setter.turtlerun.user.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import trend_setter.turtlerun.user.jwt.TokenProvider;
import trend_setter.turtlerun.user.jwt.TokenValidator;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final TokenProvider tokenProvider;
    private final TokenValidator tokenValidator;

    public Authentication authenticateWithAccessToken(String token, HttpServletRequest request) {
        if (tokenValidator.validateToken(token)) {
            return processToken(token, request);
        } else {
            throw new ExpiredJwtException(null, null, "유효하지 않은 토큰입니다.");
        }
    }

    public Authentication processToken(String token, HttpServletRequest request) {
        UserDetails userDetails = tokenValidator.extractUserDetailsFromToken(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    public Authentication refreshAccessToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = tokenValidator.extractUserDetailsFromToken(refreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String newAccessToken = tokenProvider.generateAccessToken(authentication);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
