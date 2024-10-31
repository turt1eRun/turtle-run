package trend_setter.turtlerun.inquiry.utils;

import trend_setter.turtlerun.inquiry.dto.InquiryResponseDto;
import trend_setter.turtlerun.inquiry.entity.Inquiry;
import trend_setter.turtlerun.inquiry.entity.InquiryResponse;
import trend_setter.turtlerun.user.entity.User;

public class InquiryResponseMapper {

    // InquiryResponseDto -> InquiryResponse 엔티티
    public static InquiryResponse toInquiryResponseEntity(InquiryResponseDto responseDto,
        Inquiry inquiry, User admin) {
        return InquiryResponse.builder()
            .inquiry(inquiry)
            .user(admin)
            .response(responseDto.response())
            .build();
    }

    // InquiryResponse 엔티티 -> InquiryResponseDto
    public static InquiryResponseDto toInquiryResponseDto(InquiryResponse inquiryResponse) {
        return new InquiryResponseDto(
            inquiryResponse.getResponse()
        );
    }
}
