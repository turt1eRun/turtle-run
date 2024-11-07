package trend_setter.turtlerun.global.error.exception;

import trend_setter.turtlerun.global.error.code.ErrorCode;

public class ContentException extends BusinessException {

    public ContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
