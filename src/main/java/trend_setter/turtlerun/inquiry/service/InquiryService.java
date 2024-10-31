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

    // 게시글 목록 조회
    public List<InquiryListDto> getInquiries() {
        return inquiryRepository.findAll().stream().map(
            inquiry -> new InquiryListDto(inquiry.getId(), inquiry.getTitle(),
                inquiry.getInquiryStatus())).collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public InquiryDetailDto getInquiryDetails(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 문의를 찾을 수 없습니다: " + id));
        return InquiryMapper.toResponseDto(inquiry);
    }

    // 게시글 작성
    @Transactional
    public Long createInquiry(InquiryWriteDto requestDto, String nickname) {
        User user = userRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저를 찾을 수 없습니다: " + nickname));
        Inquiry inquiry = InquiryMapper.toEntity(requestDto, user);
        inquiryRepository.save(inquiry);
        return inquiry.getId();
    }

    // 본인이 작성한 글 조회
    public List<InquiryListDto> getMyInquiries() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return inquiryRepository.findByUser_Email(currentUserEmail).stream()
            .map(InquiryMapper::toInquiryListDto)
            .collect(Collectors.toList());
    }

    // 답글 달기
    @Transactional
    public InquiryResponseDto createResponse(Long id, InquiryResponseDto responseDto,
        Long adminId) {
        Inquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 문의를 찾을 수 없습니다: " + id));

        User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 관리자를 찾을 수 없습니다: " + adminId));

        inquiry.updateInquiryStatus(responseDto);
        inquiryRepository.save(inquiry);

        InquiryResponse response = InquiryResponseMapper.toInquiryResponseEntity(responseDto,
            inquiry, admin);
        InquiryResponse savedResponse = inquiryResponseRepository.save(response);

        return InquiryResponseMapper.toInquiryResponseDto(savedResponse);
    }

    // 제목으로 검색
    public List<InquiryListDto> searchInquiriesByTitle(String title) {
        return inquiryRepository.findByTitleContaining(title).stream()
            .map(InquiryMapper::toInquiryListDto).collect(Collectors.toList());
    }

    // 닉네임으로 검색
    public List<InquiryListDto> searchInquiriesByNickname(String nickname) {
        return inquiryRepository.findByUser_NicknameContaining(nickname).stream()
            .map(InquiryMapper::toInquiryListDto).collect(Collectors.toList());
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
