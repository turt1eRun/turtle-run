package trend_setter.turtlerun.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trend_setter.turtlerun.report.entity.ConnectionReport;

public interface ConnectionReportRepository extends JpaRepository<ConnectionReport, Long>, ConnectionReportRepositoryCustom {

}
