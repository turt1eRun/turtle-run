package trend_setter.turtlerun.inquiry.dto;

import trend_setter.turtlerun.inquiry.entity.InquiryStatus;

public record InquiryStatusUpdateDto(

    InquiryStatus newStatus // 변경할 상태
) implements InquiryDto {}
