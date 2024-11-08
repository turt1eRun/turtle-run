package trend_setter.turtlerun.inquiry.dto;

import trend_setter.turtlerun.inquiry.entity.InquiryStatus;

public record InquiryListDto(

    Long id, // 문의 ID
    String title, // 제목
    InquiryStatus inquiryStatus // 상태
) implements InquiryDto {}
