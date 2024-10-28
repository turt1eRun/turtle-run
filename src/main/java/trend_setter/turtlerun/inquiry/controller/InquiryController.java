package trend_setter.turtlerun.inquiry.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trend_setter.turtlerun.inquiry.dto.InquiryListDto;
import trend_setter.turtlerun.inquiry.dto.InquiryWriteDto;
import trend_setter.turtlerun.inquiry.dto.InquiryDetailDto;
import trend_setter.turtlerun.inquiry.service.InquiryService;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<InquiryListDto>> getInquiries() {
        List<InquiryListDto> inquiries = inquiryService.getInquiries();
        return ResponseEntity.ok(inquiries);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<InquiryDetailDto> getInquiryDetails(@PathVariable Long id) {
        InquiryDetailDto inquiry = inquiryService.getInquiryDetails(id);
        return ResponseEntity.ok(inquiry);
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<Long> createInquiry(@RequestBody InquiryWriteDto requestDto,
        @RequestParam String userEmail) {
        Long inquiryId = inquiryService.createInquiry(requestDto, userEmail);
        return ResponseEntity.ok(inquiryId);
    }
}
