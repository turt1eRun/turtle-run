package trend_setter.turtlerun.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.content.constant.ContentDirectory;
import trend_setter.turtlerun.content.dto.GetFileResponse;
import trend_setter.turtlerun.content.entity.ThumbnailFile;
import trend_setter.turtlerun.content.repository.ThumbnailFileRepository;
import trend_setter.turtlerun.global.infra.s3.service.ImageValidator;
import trend_setter.turtlerun.global.infra.s3.service.S3SimpleUploader;
import trend_setter.turtlerun.global.infra.s3.util.S3KeyGenerator;

@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final S3SimpleUploader s3SimpleUploader;
    private final ImageValidator imageValidator;
    private final ThumbnailFileRepository thumbnailFileRepository;

    @Transactional
    public GetFileResponse uploadThumbnail(MultipartFile file) {
        imageValidator.validateThumbnail(file);
        String fileName = S3KeyGenerator.createFileName();
        String filePath = S3KeyGenerator.createFilePath(ContentDirectory.THUMBNAIL, fileName);
        s3SimpleUploader.upload(file, filePath);
        String originalFileName = file.getOriginalFilename();

        ThumbnailFile thumbnailFile = thumbnailFileRepository.save(
            new ThumbnailFile(fileName, originalFileName,  filePath));
        return GetFileResponse.from(thumbnailFile);
    }
}
