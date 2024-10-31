package trend_setter.turtlerun.global.error.exception;

import trend_setter.turtlerun.global.error.code.ErrorCode;

public class FileException extends BusinessException {

    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }
}