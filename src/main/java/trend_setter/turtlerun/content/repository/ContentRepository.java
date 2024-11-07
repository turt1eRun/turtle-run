package trend_setter.turtlerun.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.content.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
