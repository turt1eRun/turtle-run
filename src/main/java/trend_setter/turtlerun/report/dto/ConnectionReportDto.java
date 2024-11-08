package trend_setter.turtlerun.report.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import trend_setter.turtlerun.connection.dto.GetConnectionResponse;
import trend_setter.turtlerun.report.constant.ReportStatus;
import trend_setter.turtlerun.report.entity.ConnectionReport;

@Getter
public class ConnectionReportDto {

    private Long id;
//    private UserDto reporter;
//    private GetConnectionResponse connection;
    private String reason;
    private ReportStatus status;
    private LocalDateTime processedAt;

    @QueryProjection
    public ConnectionReportDto(ConnectionReport connectionReport) {
        this.id = connectionReport.getId();
//        this.reporter = new UserDto(connectionReport.getReporter());
//        this.connection = GetConnectionResponse.from(connectionReport.getConnection());
        this.reason = connectionReport.getReason();
        this.status = connectionReport.getStatus();
        this.processedAt = connectionReport.getProcessedAt();
    }
}
