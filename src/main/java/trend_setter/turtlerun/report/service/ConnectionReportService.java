package trend_setter.turtlerun.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trend_setter.turtlerun.connection.entity.Connection;
import trend_setter.turtlerun.connection.repository.ConnectionRepository;
import trend_setter.turtlerun.report.dto.ConnectionReportDto;
import trend_setter.turtlerun.report.dto.CreateReportRequest;
import trend_setter.turtlerun.report.entity.ConnectionReport;
import trend_setter.turtlerun.report.repository.ConnectionReportRepository;
import trend_setter.turtlerun.user.entity.User;
import trend_setter.turtlerun.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConnectionReportService {

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final ConnectionReportRepository connectionReportRepository;

    @Transactional
    public Long reportConnection(CreateReportRequest request) {
        User user = userRepository.findByEmail("test1@naver.com") //todo 추후에 UserDetail 의 username 으로 수정 필요
            .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Connection connection = connectionRepository.findById(request.getId())
            .orElseThrow(() -> new IllegalArgumentException("해당 커넥션을 찾을 수 없습니다."));
        ConnectionReport report = ConnectionReport.builder()
            .reporter(user)
            .connection(connection)
            .reason(request.getReason())
            .build();
        connectionReportRepository.save(report);

        return report.getId();
    }

    @Transactional
    public Long processReport(Long id) {
        return null;
    }

    public Page<ConnectionReportDto> findConnectionReports(Pageable pageable) {
        return connectionReportRepository.findPendingConnectionReports(pageable);
    }
}
