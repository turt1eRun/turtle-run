package trend_setter.turtlerun.inquiry.dto;

import trend_setter.turtlerun.inquiry.entity.InquiryStatus;

public record InquiryDetailDto(

    Long id, // 문의 ID
    String title, // 제목
    String content, // 내용
    InquiryStatus inquiryStatus, // 상태
    String nickname // 작성자 닉네임
) implements InquiryDto {}
