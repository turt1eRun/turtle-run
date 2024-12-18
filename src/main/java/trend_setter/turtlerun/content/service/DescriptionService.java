package trend_setter.turtlerun.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.content.constant.ContentDirectory;
import trend_setter.turtlerun.content.dto.GetFileResponse;
import trend_setter.turtlerun.content.entity.DescriptionFile;
import trend_setter.turtlerun.content.repository.DescriptionFileRepository;
import trend_setter.turtlerun.global.infra.s3.service.ImageValidator;
import trend_setter.turtlerun.global.infra.s3.service.S3SimpleUploader;
import trend_setter.turtlerun.global.infra.s3.util.S3KeyGenerator;

@Service
@RequiredArgsConstructor
public class DescriptionService {

    private final S3SimpleUploader s3SimpleUploader;
    private final ImageValidator imageValidator;
    private final DescriptionFileRepository descriptionFileRepository;

    @Transactional
    public GetFileResponse uploadDescriptionImage(MultipartFile file) {
        imageValidator.validateDescriptionImage(file);
        String fileName = S3KeyGenerator.createFileName();
        String filePath = S3KeyGenerator.createFilePath(ContentDirectory.DESCRIPTION, fileName);
        s3SimpleUploader.upload(file, filePath);

        DescriptionFile descriptionFile = descriptionFileRepository.save(
            new DescriptionFile(fileName, filePath));
        return GetFileResponse.from(descriptionFile);
    }
}


