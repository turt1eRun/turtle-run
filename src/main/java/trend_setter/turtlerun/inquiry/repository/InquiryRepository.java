package trend_setter.turtlerun.inquiry.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.inquiry.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 이메일을 기준으로 작성된 게시글 조회
    List<Inquiry> findByUser_Email(String email);

    // 제목을 기준으로 검색
    Page<Inquiry> findByTitleContaining(String title, Pageable pageable);

    // 닉네임을 기준으로 검색
    Page<Inquiry> findByUser_NicknameContaining(String nickname, Pageable pageable);
}
