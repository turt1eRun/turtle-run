package trend_setter.turtlerun.global.error.exception;

import lombok.Getter;
import trend_setter.turtlerun.global.error.code.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}