package trend_setter.turtlerun.inquiry.utils;

import trend_setter.turtlerun.inquiry.dto.InquiryListDto;
import trend_setter.turtlerun.inquiry.dto.InquiryWriteDto;
import trend_setter.turtlerun.inquiry.dto.InquiryDetailDto;
import trend_setter.turtlerun.inquiry.entity.Inquiry;
import trend_setter.turtlerun.user.entity.User;

public class InquiryMapper {

    // InquiryWriteDto -> Inquiry 엔티티
    public static Inquiry toEntity(InquiryWriteDto dto, User user) {
        return Inquiry.builder().user(user).title(dto.title()).content(dto.content()).build();
    }

    // Inquiry 엔티티 -> InquiryDetailDto
    public static InquiryDetailDto toResponseDto(Inquiry inquiry) {
        return new InquiryDetailDto(inquiry.getId(), inquiry.getTitle(), inquiry.getContent(),
            inquiry.getInquiryStatus(), inquiry.getUser().getNickname());
    }

    // Inquiry 엔티티 -> InquiryListDto
    public static InquiryListDto toInquiryListDto(Inquiry inquiry) {
        return new InquiryListDto(
            inquiry.getId(),
            inquiry.getTitle(),
            inquiry.getInquiryStatus()
        );
    }
}
