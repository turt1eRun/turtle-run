package trend_setter.turtlerun.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private final String status;
    private final T data;
    private final String message;
    private final String code;

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
            .status("success")
            .data(data)
            .message("Successfully created")
            .code("201")
            .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
            .status("success")
            .data(data)
            .message("Successfully retrieved")
            .code("200")
            .build();
    }
}
