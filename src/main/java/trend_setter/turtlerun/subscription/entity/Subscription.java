package trend_setter.turtlerun.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.global.common.BaseTimeEntity;
import trend_setter.turtlerun.user.entity.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptions")
@Getter
public class Subscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id; // 구독 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribed_to", nullable = false)
    private User subscribedTo; // 구독 대상 ID

    @Builder
    public Subscription(User user, User subscribedTo) {
        this.user = user;
        this.subscribedTo = subscribedTo;
    }
}
