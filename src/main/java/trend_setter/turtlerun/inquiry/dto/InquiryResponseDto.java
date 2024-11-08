package trend_setter.turtlerun.inquiry.dto;

public record InquiryResponseDto(

    String response, // 답글 내용
    String adminNickname // 관리자 닉네임
) implements InquiryDto {}
