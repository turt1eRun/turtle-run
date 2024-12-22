package trend_setter.turtlerun.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.global.common.BaseEntity;
import trend_setter.turtlerun.global.infra.s3.entity.S3Deletable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "thumbnail_files")
public class ThumbnailFile extends BaseEntity implements S3Deletable {

    @Id @Column(name = "thumbnail_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private LocalDateTime deletedAt;

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }

    public ThumbnailFile(String fileName, String originalFileName, String filePath) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
    }

    @Builder(builderMethodName = "testBuilder")
    private ThumbnailFile(String fileName, String originalFileName, String filePath,
        LocalDateTime deletedAt) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.deletedAt = deletedAt;
    }

    public ThumbnailFile(Long id) {
        this.id = id;
    }
}