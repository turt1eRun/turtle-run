package trend_setter.turtlerun.inquiry.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import trend_setter.turtlerun.global.common.BaseTimeEntity;
import trend_setter.turtlerun.user.entity.User;

@Entity
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

  protected Inquiry() {}

  @Builder
  public Inquiry(User user, String title, String content, InquiryStatus inquiryStatus) {
    this.user = user;
    this.title = title;
    this.content = content;
    this.inquiryStatus = inquiryStatus != null ? inquiryStatus : InquiryStatus.PENDING;
  }
}