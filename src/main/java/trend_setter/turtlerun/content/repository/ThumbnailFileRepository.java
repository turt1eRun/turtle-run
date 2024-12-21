package trend_setter.turtlerun.content.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trend_setter.turtlerun.content.entity.ThumbnailFile;

public interface ThumbnailFileRepository extends JpaRepository<ThumbnailFile, Long> {

    Page<ThumbnailFile> findByDeletedAtBefore(LocalDateTime threshold, Pageable pageable);

    @Query("SELECT t FROM ThumbnailFile t "
        + "WHERE t.createdAt < :threshold "
        + "AND t.deletedAt IS NULL "
        + "AND NOT EXISTS ("
        + "    SELECT 1 FROM Content c WHERE c.thumbnail = t"
        + ")")
    Page<ThumbnailFile> findOrphanFiles(@Param("threshold")LocalDateTime threshold, Pageable pageable);
}
