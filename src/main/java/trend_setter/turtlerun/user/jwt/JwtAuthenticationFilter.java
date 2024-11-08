package trend_setter.turtlerun.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import trend_setter.turtlerun.user.service.CookieService;
import trend_setter.turtlerun.user.service.JwtAuthenticationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenValidator tokenValidator;
    private final CookieService cookieService;
    private final JwtAuthenticationService jwtAuthenticationService;

    private static final List<String> PUBLIC_PATHS = Arrays.asList("/api/users/login", "/api/users/register");

    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenValidator.extractTokenFromHeader(httpServletRequest);
        String refreshToken = cookieService.extractTokenFromCookie(httpServletRequest, "refresh_token");

        Authentication authentication = null;
        boolean tokenRefreshed = false;

        if(accessToken != null) {
            authentication = jwtAuthenticationService.authenticateWithAccessToken(accessToken, httpServletRequest);
        }

        if(authentication == null && refreshToken != null) {
            authentication = jwtAuthenticationService.refreshAccessToken(refreshToken, httpServletRequest, httpServletResponse);
            accessToken = tokenValidator.extractAccessTokenFromResponseHeader(httpServletResponse);
            tokenRefreshed = true;
        }

        if(authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if(tokenRefreshed) {
                httpServletRequest = new CustomHttpServletRequestWrapper(httpServletRequest);
                ((CustomHttpServletRequestWrapper)httpServletRequest).putHeader("Authorization", "Bearer " + accessToken);
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write("Authentication failed. Please log in again.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        boolean shouldSkip = PUBLIC_PATHS.stream().anyMatch(path::startsWith) || path.equals("/");
        log.debug("Should skip filter for path {}: {}", path, shouldSkip);

        return shouldSkip;
    }
}
