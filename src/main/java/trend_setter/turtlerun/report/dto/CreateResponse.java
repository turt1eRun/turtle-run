package trend_setter.turtlerun.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateResponse {

    private final Long id;

    public static CreateResponse of(Long id) {
        return CreateResponse.builder()
            .id(id)
            .build();
    }
}
