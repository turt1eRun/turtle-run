package trend_setter.turtlerun.inquiry.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trend_setter.turtlerun.inquiry.dto.InquiryListDto;
import trend_setter.turtlerun.inquiry.dto.InquiryResponseDto;
import trend_setter.turtlerun.inquiry.dto.InquiryStatusUpdateDto;
import trend_setter.turtlerun.inquiry.dto.InquiryWriteDto;
import trend_setter.turtlerun.inquiry.dto.InquiryDetailDto;
import trend_setter.turtlerun.inquiry.service.InquiryService;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // 게시글 목록 조회 (관리자 전용)
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<InquiryListDto>> getInquiries(Pageable pageable) {
        Page<InquiryListDto> inquiries = inquiryService.getInquiries(pageable);
        return ResponseEntity.ok(inquiries);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @inquiryService.isAuthor(#id)")
    public ResponseEntity<InquiryDetailDto> getInquiryDetails(@PathVariable Long id) {
        InquiryDetailDto inquiry = inquiryService.getInquiryDetails(id);
        return ResponseEntity.ok(inquiry);
    }

    // 게시글 작성
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InquiryDetailDto> createInquiry(@RequestBody InquiryWriteDto requestDto) {
        InquiryDetailDto inquiry = inquiryService.createInquiry(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiry);
    }

    // 본인이 작성한 글 목록 조회
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InquiryListDto>> getMyInquiries() {
        List<InquiryListDto> myInquiries = inquiryService.getMyInquiries();
        return ResponseEntity.ok(myInquiries);
    }

    // 게시글에 답글 달기 (관리자 전용)
    @PostMapping("/{id}/response")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InquiryResponseDto> createResponse(@PathVariable Long id,
        @RequestBody InquiryResponseDto responseDto) {
        InquiryResponseDto createdResponse = inquiryService.createResponse(id, responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
    }

    // 제목으로 검색하기
    @GetMapping("/search/title")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<InquiryListDto>> searchInquiriesByTitle(
        @RequestParam String keyword, Pageable pageable) {
        Page<InquiryListDto> results = inquiryService.searchInquiriesByTitle(keyword, pageable);
        return ResponseEntity.ok(results);
    }

    // 닉네임으로 검색하기
    @GetMapping("/search/nickname")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<InquiryListDto>> searchInquiriesByNickname(
        @RequestParam String nickname, Pageable pageable) {
        Page<InquiryListDto> results = inquiryService.searchInquiriesByNickname(nickname, pageable);
        return ResponseEntity.ok(results);
    }

    // 게시글 상태 변경
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateInquiryStatus(@PathVariable Long id,
        @RequestBody InquiryStatusUpdateDto statusUpdateDto) {
        inquiryService.updateInquiryStatus(id, statusUpdateDto);
        return ResponseEntity.noContent().build();
    }
}
