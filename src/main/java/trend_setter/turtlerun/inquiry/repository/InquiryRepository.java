package trend_setter.turtlerun.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.inquiry.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

}
