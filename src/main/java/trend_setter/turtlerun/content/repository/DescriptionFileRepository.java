package trend_setter.turtlerun.content.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.content.entity.DescriptionFile;

public interface DescriptionFileRepository extends JpaRepository<DescriptionFile, Long> {
    List<DescriptionFile> findByDeletedAtBefore(LocalDateTime localDateTime);
}
