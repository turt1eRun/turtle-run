package trend_setter.turtlerun.inquiry.dto;

import trend_setter.turtlerun.inquiry.entity.InquiryStatus;

public record InquiryDetailDto(

    Long id, // 문의 ID
    String title, // 제목
    String content, // 내용
    InquiryStatus inquiryStatus, // 상태
    String userEmail // 작성자 이메일
) implements InquiryDto {}
