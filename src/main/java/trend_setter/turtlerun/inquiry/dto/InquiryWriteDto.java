package trend_setter.turtlerun.inquiry.dto;

public record InquiryWriteDto(

    String title, // 제목
    String content, // 내용
    String nickname // 작성자
) implements InquiryDto {}
