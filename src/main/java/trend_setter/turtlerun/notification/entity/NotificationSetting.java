package trend_setter.turtlerun.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification_settings")
@Getter
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long id; // 알림 설정 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType; // 알림 타입

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isEnabled = true; // 알림 허용 여부

    @Builder
    public NotificationSetting(User user, NotificationType notificationType, Boolean isEnabled) {
        this.user = user;
        this.notificationType = notificationType;
        this.isEnabled = (isEnabled != null) ? isEnabled : true;
    }
}
