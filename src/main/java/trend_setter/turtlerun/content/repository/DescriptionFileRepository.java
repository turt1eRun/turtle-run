package trend_setter.turtlerun.content.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.content.entity.DescriptionFile;

public interface DescriptionFileRepository extends JpaRepository<DescriptionFile, Long> {
    Page<DescriptionFile> findByDeletedAtBefore(LocalDateTime localDateTime, Pageable pageable);
}
