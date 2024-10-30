package trend_setter.turtlerun.report.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import trend_setter.turtlerun.report.constant.ReportStatus;
import trend_setter.turtlerun.report.entity.ConnectionReport;

@Getter
public class ConnectionReportDto {

    private Long id;
//    private UserDto reporter;
//    private ConnectionDto connection;
    private String reason;
    private ReportStatus status;
    private LocalDateTime processedAt;

    public ConnectionReportDto(ConnectionReport connectionReport) {
        this.id = connectionReport.getId();
//        this.reporter = new UserDto(connectionReport.getReporter());
//        this.connection = new ConnectionDto(connectionReport.getConnection());
        this.reason = connectionReport.getReason();
        this.status = connectionReport.getStatus();
        this.processedAt = connectionReport.getProcessedAt();
    }
}
