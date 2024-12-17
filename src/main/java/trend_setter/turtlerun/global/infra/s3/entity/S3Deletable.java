package trend_setter.turtlerun.global.infra.s3.entity;

import java.time.LocalDateTime;

public interface S3Deletable {
    String getFilePath();
    LocalDateTime getDeletedAt();
}
