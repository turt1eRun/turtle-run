package trend_setter.turtlerun.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trend_setter.turtlerun.report.dto.ApiResponse;
import trend_setter.turtlerun.report.dto.ConnectionReportDto;
import trend_setter.turtlerun.report.dto.CreateReportRequest;
import trend_setter.turtlerun.report.dto.CreateResponse;
import trend_setter.turtlerun.report.service.ConnectionReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connection-reports")
public class ConnectionReportController {

    private final ConnectionReportService connectionReportService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateResponse>> createConnectionReport(
        @RequestBody CreateReportRequest request
    ) {
        Long reportId = connectionReportService.reportConnection(request);
        CreateResponse createResponse = CreateResponse.of(reportId);
        ApiResponse<CreateResponse> response = ApiResponse.success(createResponse);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ConnectionReportDto>> getConnectionReports(
        Pageable pageable
    ) {
        return ResponseEntity.ok(connectionReportService.findConnectionReports(pageable));
    }


}
