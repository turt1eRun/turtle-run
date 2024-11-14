package trend_setter.turtlerun.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.global.common.BaseEntity;
import trend_setter.turtlerun.user.constant.Role;

@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isEmailVerified;
    private LocalDateTime deletedAt;
    private int suspensionCount;
    private LocalDateTime suspendedAt;
    private boolean isPermanentlySuspended;

    public void emailVerificationComplete() {
        this.isEmailVerified = true;
        this.role = Role.TURTLE;
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public void increaseSuspensionCount() {
        this.suspensionCount++;
    }

    public void setSuspendedAt() {
        this.suspendedAt = LocalDateTime.now();
    }

    public void setPermanentlySuspended() {
        this.isPermanentlySuspended = true;
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
