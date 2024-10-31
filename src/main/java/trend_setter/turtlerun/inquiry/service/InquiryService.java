package trend_setter.turtlerun.inquiry.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import trend_setter.turtlerun.inquiry.dto.InquiryListDto;
import trend_setter.turtlerun.inquiry.dto.InquiryResponseDto;
import trend_setter.turtlerun.inquiry.dto.InquiryStatusUpdateDto;
import trend_setter.turtlerun.inquiry.dto.InquiryWriteDto;
import trend_setter.turtlerun.inquiry.dto.InquiryDetailDto;
import trend_setter.turtlerun.inquiry.entity.Inquiry;
import trend_setter.turtlerun.inquiry.entity.InquiryResponse;
import trend_setter.turtlerun.inquiry.repository.InquiryResponseRepository;
import trend_setter.turtlerun.inquiry.utils.InquiryMapper;
import trend_setter.turtlerun.inquiry.repository.InquiryRepository;
import trend_setter.turtlerun.inquiry.utils.InquiryResponseMapper;
import trend_setter.turtlerun.user.entity.User;
import trend_setter.turtlerun.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryResponseRepository inquiryResponseRepository;
    private final UserRepository userRepository;

    // 현재 인증된 사용자의 이메일을 가져오는 메서드
    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 현재 사용자가 특정 문의의 작성자인지 확인
    public boolean isAuthor(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
            .map(inquiry -> inquiry.getUser().getEmail().equals(getCurrentUserEmail()))
            .orElse(false);
    }

    // 게시글 목록 조회
    public List<InquiryListDto> getInquiries() {
        return inquiryRepository.findAll().stream()
            .map(InquiryMapper::toInquiryListDto)
            .collect(Collectors.toList());
    }

    // 게시글 상세 조회 (작성자 또는 관리자)
    public InquiryDetailDto getInquiryDetails(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 문의를 찾을 수 없습니다: " + id));
        return InquiryMapper.toResponseDto(inquiry);
    }

    // 게시글 작성
    @Transactional
    public InquiryDetailDto createInquiry(InquiryWriteDto requestDto) {
        User user = userRepository.findByEmail(getCurrentUserEmail())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Inquiry inquiry = InquiryMapper.toEntity(requestDto, user);
        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        return InquiryMapper.toResponseDto(savedInquiry);
    }

    // 본인이 작성한 글 조회
    public List<InquiryListDto> getMyInquiries() {
        return inquiryRepository.findByUser_Email(getCurrentUserEmail()).stream()
            .map(InquiryMapper::toInquiryListDto)
            .collect(Collectors.toList());
    }

    // 답글 달기
    @Transactional
    public InquiryResponseDto createResponse(Long id, InquiryResponseDto responseDto) {
        Inquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 문의를 찾을 수 없습니다: " + id));

        User admin = userRepository.findByEmail(getCurrentUserEmail())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        inquiry.updateInquiryStatus(responseDto);
        inquiryRepository.save(inquiry);

        InquiryResponse response = InquiryResponseMapper.toInquiryResponseEntity(responseDto,
            inquiry, admin);
        InquiryResponse savedResponse = inquiryResponseRepository.save(response);

        return InquiryResponseMapper.toInquiryResponseDto(savedResponse);
    }

    // 제목으로 검색 (관리자 전용)
    public List<InquiryListDto> searchInquiriesByTitle(String title) {
        return inquiryRepository.findByTitleContaining(title).stream()
            .map(InquiryMapper::toInquiryListDto)
            .collect(Collectors.toList());
    }

    // 닉네임으로 검색 (관리자 전용)
    public List<InquiryListDto> searchInquiriesByNickname(String nickname) {
        return inquiryRepository.findByUser_NicknameContaining(nickname).stream()
            .map(InquiryMapper::toInquiryListDto)
            .collect(Collectors.toList());
    }

    // 게시글 상태 변경
    @Transactional
    public void updateInquiryStatus(Long id, InquiryStatusUpdateDto statusUpdateDto) {
        Inquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 문의를 찾을 수 없습니다: " + id));

        inquiry.updateInquiryStatus(statusUpdateDto);
        inquiryRepository.save(inquiry);
    }
}
