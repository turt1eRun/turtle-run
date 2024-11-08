package trend_setter.turtlerun.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification_receipts")
@Getter
public class NotificationReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id; // 알림 수신 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification; // 알림 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false)
    private NotificationSetting notificationSetting; // 알림 설정 ID

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean isRead = false; // 알림 읽음 여부

    @Builder
    public NotificationReceipt(Notification notification, NotificationSetting notificationSetting,
        Boolean isRead) {
        this.notification = notification;
        this.notificationSetting = notificationSetting;
        this.isRead = (isRead != null) ? isRead : false;
    }
}
