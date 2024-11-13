package trend_setter.turtlerun.global.config;

import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    private static final String MAIL_SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String MAIL_SMTP_WRITE_TIMEOUT = "mail.smtp.wrtietimeout";
    private static final String MAIL_DEBUG = "mail.smtp.debug";

    // SMTP 서버
    @Value("${spring.mail.host}")
    private String host;

    // 포트번호
    @Value("${spring.mail.port}")
    private int port;

    // 계정
    @Value("${spring.mail.username}")
    private String username;

    // 비밀번호
    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean tlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean tlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;

    @Value("${spring.mail.properties.mail.smtp.debug}")
    private boolean debug;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);

        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, auth);
        properties.put(MAIL_STARTTLS_ENABLE, tlsEnable);
        properties.put(MAIL_STARTTLS_REQUIRED, tlsRequired);
        properties.put(MAIL_SMTP_CONNECTION_TIMEOUT, connectionTimeout);
        properties.put(MAIL_SMTP_TIMEOUT, timeout);
        properties.put(MAIL_SMTP_WRITE_TIMEOUT, writeTimeout);
        properties.put(MAIL_DEBUG, debug);

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }
}