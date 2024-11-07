package trend_setter.turtlerun.connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.connection.entity.Connection;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

}
