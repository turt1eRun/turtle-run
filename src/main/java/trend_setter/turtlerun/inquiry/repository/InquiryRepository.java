package trend_setter.turtlerun.inquiry.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.inquiry.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 제목을 기준으로 검색
    List<Inquiry> findByTitleContaining(String title);

    // 닉네임을 기준으로 검색
    List<Inquiry> findByUser_NicknameContaining(String nickname);
}
