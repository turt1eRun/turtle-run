package trend_setter.turtlerun.inquiry.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import trend_setter.turtlerun.global.common.BaseTimeEntity;
import trend_setter.turtlerun.user.User;

@Entity
@Table(name = "inquiry_responses")
@Getter
public class InquiryResponse extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private long id; // 답변 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User user; // 답변한 관리자 (users 테이블의 외래 키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry; // 문의 ID (inquiries 테이블의 외래 키)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String response; // 답변 내용

    protected InquiryResponse() {
    }

    @Builder
    public InquiryResponse(User user, Inquiry inquiry, String response) {
        this.user = user;
        this.inquiry = inquiry;
        this.response = response;
    }
}
