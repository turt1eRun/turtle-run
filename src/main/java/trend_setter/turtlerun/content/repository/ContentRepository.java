package trend_setter.turtlerun.content.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trend_setter.turtlerun.content.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long>, ContentRepositoryCustom {
    @Query("SELECT c "
        + "FROM Content c "
        + "JOIN FETCH c.creator "
        + "JOIN FETCH c.video "
        + "JOIN FETCH c.thumbnail "
        + "LEFT JOIN FETCH c.descriptionBlocks b "
        + "LEFT JOIN FETCH b.descriptionFile "
        + "WHERE c.id = :contentId")
    Optional<Content> findByIdWithAllRelations(@Param("contentId") Long contentId);
}
