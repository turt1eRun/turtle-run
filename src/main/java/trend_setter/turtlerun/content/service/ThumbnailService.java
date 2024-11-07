package trend_setter.turtlerun.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.content.constant.ContentDirectory;
import trend_setter.turtlerun.content.dto.GetFileUploadResponse;
import trend_setter.turtlerun.content.entity.ThumbnailFile;
import trend_setter.turtlerun.content.repository.ThumbnailFileRepository;
import trend_setter.turtlerun.global.infra.s3.service.ImageValidator;
import trend_setter.turtlerun.global.infra.s3.service.S3ImageUploader;
import trend_setter.turtlerun.global.infra.s3.util.S3KeyGenerator;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final S3ImageUploader s3ImageUploader;
    private final ImageValidator imageValidator;
    private final ThumbnailFileRepository thumbnailFileRepository;

    @Transactional
    public GetFileUploadResponse uploadThumbnail(MultipartFile file) {
        imageValidator.validateThumbnail(file);
        String fileName = S3KeyGenerator.createFileName();
        String filePath = S3KeyGenerator.createFilePath(ContentDirectory.THUMBNAIL, fileName);
        s3ImageUploader.upload(file, filePath);

        ThumbnailFile thumbnailFile = ThumbnailFile.builder()
            .fileName(fileName).filePath(filePath).build();

        return new GetFileUploadResponse(thumbnailFileRepository.save(thumbnailFile));
    }
}
