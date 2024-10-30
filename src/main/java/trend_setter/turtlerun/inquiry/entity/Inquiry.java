package trend_setter.turtlerun.inquiry.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.global.common.BaseTimeEntity;
import trend_setter.turtlerun.inquiry.dto.InquiryDto;
import trend_setter.turtlerun.inquiry.dto.InquiryResponseDto;
import trend_setter.turtlerun.inquiry.dto.InquiryStatusUpdateDto;
import trend_setter.turtlerun.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inquiries")
@Getter
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id; // 문의 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자 ID

    @Column(length = 225, nullable = false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus inquiryStatus = InquiryStatus.PENDING; // 상태 (PENDING, ANSWERED)

    @Builder
    public Inquiry(User user, String title, String content, InquiryStatus inquiryStatus) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.inquiryStatus = inquiryStatus != null ? inquiryStatus : InquiryStatus.PENDING;
    }

    // 답변 상태 수정 메서드
    public void updateInquiryStatus(InquiryDto inquiryDto) {
        if (inquiryDto instanceof InquiryStatusUpdateDto inquiryStatusUpdateDto) {
            this.inquiryStatus = inquiryStatusUpdateDto.newStatus();
        } else if (inquiryDto instanceof InquiryResponseDto inquiryResponseDto) {
            this.inquiryStatus = InquiryStatus.ANSWERED;
        }
    }
}
