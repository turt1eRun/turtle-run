package trend_setter.turtlerun.global.infra.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.content.entity.VideoFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final VideoUploader videoUploader;

    public VideoFile uploadVideo(MultipartFile file) {
        return videoUploader.upload(file);
    }
}
