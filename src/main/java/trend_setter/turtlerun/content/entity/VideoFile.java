package trend_setter.turtlerun.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.global.infra.s3.entity.S3Deletable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "video_files")
public class VideoFile implements S3Deletable {

    @Id @Column(name = "video_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private int duration;
    private LocalDateTime deletedAt;

    public VideoFile(String fileName, String filePath, int duration) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.duration = duration;
    }

    @Builder(builderMethodName = "testBuilder")
    private VideoFile(String fileName, String filePath, int duration, LocalDateTime deletedAt) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.duration = duration;
        this.deletedAt = deletedAt;
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }

    public VideoFile(Long id) {
        this.id = id;
    }
}
