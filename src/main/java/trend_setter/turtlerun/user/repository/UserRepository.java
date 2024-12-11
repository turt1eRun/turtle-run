package trend_setter.turtlerun.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    List<User> findAllByDeletedAtBefore(LocalDateTime deletedAt);
}
