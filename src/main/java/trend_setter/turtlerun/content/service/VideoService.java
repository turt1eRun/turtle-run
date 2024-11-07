package trend_setter.turtlerun.content.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.content.constant.ContentDirectory;
import trend_setter.turtlerun.content.dto.GetFileUploadResponse;
import trend_setter.turtlerun.content.entity.VideoFile;
import trend_setter.turtlerun.content.repository.VideoFileRepository;
import trend_setter.turtlerun.global.error.code.FileErrorCode;
import trend_setter.turtlerun.global.error.exception.FileException;
import trend_setter.turtlerun.global.infra.s3.service.S3VideoUploader;
import trend_setter.turtlerun.global.infra.s3.service.VideoValidator;
import trend_setter.turtlerun.global.infra.s3.util.S3KeyGenerator;
import trend_setter.turtlerun.global.infra.s3.util.VideoMetadataExtractor;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3VideoUploader s3VideoUploader;
    private final VideoMetadataExtractor videoMetadataExtractor;
    private final VideoValidator videoValidator;
    private final VideoFileRepository videoFileRepository;

    @Transactional
    public GetFileUploadResponse uploadVideo(MultipartFile file) {
        videoValidator.validate(file);
        String fileName = S3KeyGenerator.createFileName();
        String filePath = S3KeyGenerator.createFilePath(ContentDirectory.VIDEO, fileName);
        int duration = extractDuration(file);
        Map<String, String> metadata = Map.of("duration", String.valueOf(duration));
        s3VideoUploader.upload(file, filePath, metadata);

        return new GetFileUploadResponse(videoFileRepository.save(new VideoFile(fileName, filePath, duration)));
    }

    private int extractDuration(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("video", null);
            file.transferTo(tempFile);
            double duration = videoMetadataExtractor.getVideoDuration(tempFile);
            tempFile.delete();
            return (int) Math.round(duration);
        } catch (IOException | JCodecException e) {
            throw new FileException(FileErrorCode.INVALID_VIDEO_FORMAT);
        }
    }
}