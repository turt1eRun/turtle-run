package trend_setter.turtlerun.content.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trend_setter.turtlerun.content.entity.VideoFile;

public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {

    Page<VideoFile> findByDeletedAtBefore(LocalDateTime threshold, Pageable pageable);

    @Query("SELECT v FROM VideoFile v "
        + "WHERE v.createdAt < :threshold "
        + "AND v.deletedAt IS NULL "
        + "AND NOT EXISTS ("
        + "    SELECT 1 FROM Content c WHERE c.video = v"
        + ")")
    Page<VideoFile> findOrphanFiles(@Param("threshold") LocalDateTime threshold, Pageable pageable);
}
