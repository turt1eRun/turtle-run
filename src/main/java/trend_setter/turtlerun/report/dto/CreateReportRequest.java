package trend_setter.turtlerun.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReportRequest {
    private Long id;
    private String reason;

    @Builder
    public CreateReportRequest(Long id, String reason) {
        this.id = id;
        this.reason = reason;
    }
}
