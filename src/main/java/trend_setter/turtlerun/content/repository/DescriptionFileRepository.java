package trend_setter.turtlerun.content.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trend_setter.turtlerun.content.entity.DescriptionFile;

public interface DescriptionFileRepository extends JpaRepository<DescriptionFile, Long> {

    Page<DescriptionFile> findByDeletedAtBefore(LocalDateTime threshold, Pageable pageable);

    @Query("SELECT d FROM DescriptionFile d "
        + "WHERE d.createdAt < :threshold "
        + "AND d.deletedAt IS NULL "
        + "AND NOT EXISTS ("
        + "    SELECT 1 FROM DescriptionBlock db WHERE db.descriptionFile= d"
        + ")")
    Page<DescriptionFile> findOrphanFiles(@Param("threshold")LocalDateTime threshold, Pageable pageable);
}
