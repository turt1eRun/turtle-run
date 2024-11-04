package trend_setter.turtlerun.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.content.entity.VideoFile;

public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {

}
