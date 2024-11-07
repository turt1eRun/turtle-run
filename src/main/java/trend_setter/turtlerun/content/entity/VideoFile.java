package trend_setter.turtlerun.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.content.constant.VideoStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "video_files")
public class VideoFile {

    @Id @Column(name = "video_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;
    private String fileName;
    private String filePath;
    private int duration;

    public VideoFile(String fileName, String filePath, int duration) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.duration = duration;
    }

    public VideoFile(Long id) {
        this.id = id;
    }
}
