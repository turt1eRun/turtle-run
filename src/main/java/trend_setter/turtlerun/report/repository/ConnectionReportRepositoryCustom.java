package trend_setter.turtlerun.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import trend_setter.turtlerun.report.dto.ConnectionReportDto;

public interface ConnectionReportRepositoryCustom {

    Page<ConnectionReportDto> findPendingConnectionReports(Pageable pageable);

}
