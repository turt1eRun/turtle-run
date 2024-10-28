package trend_setter.turtlerun.inquiry.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trend_setter.turtlerun.inquiry.dto.InquiryListDto;
import trend_setter.turtlerun.inquiry.dto.InquiryWriteDto;
import trend_setter.turtlerun.inquiry.dto.InquiryDetailDto;
import trend_setter.turtlerun.inquiry.entity.Inquiry;
import trend_setter.turtlerun.inquiry.utils.InquiryMapper;
import trend_setter.turtlerun.inquiry.repository.InquiryRepository;
import trend_setter.turtlerun.user.User;
import trend_setter.turtlerun.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    // 게시글 목록 조회
    @Transactional
    public List<InquiryListDto> getInquiries() {
        return inquiryRepository.findAll().stream().map(
            inquiry -> new InquiryListDto(inquiry.getId(), inquiry.getTitle(),
                inquiry.getInquiryStatus())).collect(Collectors.toList());
    }

    // 게시글 상세 조회
    @Transactional
    public InquiryDetailDto getInquiryDetails(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 문의를 찾을 수 없습니다: " + id));
        return InquiryMapper.toResponseDto(inquiry);
    }

    // 게시글 작성
    @Transactional
    public Long createInquiry(InquiryWriteDto requestDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
            () -> new IllegalArgumentException("해당 이메일의 유저를 찾을 수 없습니다: " + userEmail));
        Inquiry inquiry = InquiryMapper.toEntity(requestDto, user);
        inquiryRepository.save(inquiry);
        return inquiry.getId();
    }
}
