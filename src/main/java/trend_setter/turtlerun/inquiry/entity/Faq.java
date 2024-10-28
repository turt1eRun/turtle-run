package trend_setter.turtlerun.inquiry.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import trend_setter.turtlerun.global.common.BaseTimeEntity;
import trend_setter.turtlerun.user.User;

@Entity
@Table(name = "faqs")
@Getter
public class Faq extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private long id; // 문의 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User user; // 작성자 ID (users 테이블의 외래 키)

    @Column(length = 225, nullable = false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    protected Faq() {
    }

    @Builder
    public Faq(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }
}
