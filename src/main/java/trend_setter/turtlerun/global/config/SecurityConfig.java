package trend_setter.turtlerun.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            // 비회원 공개 엔드포인트: 루트 URL, 사용자 로그인 API
            .requestMatchers("/", "/api/users/login", "/api/content-reports/**", "/api/connection-reports/**"
            , "/api/report-histories/**", "/api/report-reasons/**", "/api/ai/**", "/api/contents/**"
            , "/api/connections/**", "/api/comments/**", "/api/questions/**", "/api/subscriptions/**"
            , "/api/likes/**", "/api/inquiries/**", "/api/faqs/**", "/api/notifications/**")
            .permitAll()

            // 비회원 공개 엔드포인트: 사용자 등록 API
            .requestMatchers(HttpMethod.POST, "/api/users/register")
            .permitAll()

            // 이 외 모든 요청은 인증을 받아야한다.
            .anyRequest()
            .authenticated()
        );

        http.httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 기본적으로 bcrypt 암호화 알고리즘의 BCryptPasswordEncoder 객체를 생성하고 사용하게 된다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
