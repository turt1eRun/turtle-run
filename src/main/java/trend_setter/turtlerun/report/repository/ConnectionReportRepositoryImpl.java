package trend_setter.turtlerun.report.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import trend_setter.turtlerun.report.constant.ReportStatus;
import trend_setter.turtlerun.report.dto.ConnectionReportDto;
import trend_setter.turtlerun.report.dto.QConnectionReportDto;
import trend_setter.turtlerun.report.entity.QConnectionReport;

@Repository
@RequiredArgsConstructor
public class ConnectionReportRepositoryImpl implements ConnectionReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QConnectionReport connectionReport = QConnectionReport.connectionReport;

    @Override
    public Page<ConnectionReportDto> findPendingConnectionReports(Pageable pageable) {
        List<ConnectionReportDto> reports = queryFactory
            .select(new QConnectionReportDto(connectionReport))
            .from(connectionReport)
            .where(connectionReport.status.eq(ReportStatus.PENDING))
            .orderBy(connectionReport.createdAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> count = queryFactory
            .select(connectionReport.count())
            .from(connectionReport)
            .where(connectionReport.status.eq(ReportStatus.PENDING));

        return PageableExecutionUtils.getPage(reports, pageable, count::fetchOne);
    }
}
