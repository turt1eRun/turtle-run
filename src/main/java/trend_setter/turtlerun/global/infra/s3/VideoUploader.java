package trend_setter.turtlerun.global.infra.s3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import trend_setter.turtlerun.content.constant.ContentDirectory;
import trend_setter.turtlerun.content.entity.VideoFile;
import trend_setter.turtlerun.global.util.FilePathUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoUploader {

    private static final int CHUNK_SIZE = 5 * 1024 * 1024; //5MB
    private final S3Client s3Client;
    private final VideoValidator validator;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public VideoFile upload(MultipartFile file) {
        validator.validate(file);

        String fileName = FilePathUtils.createFileName();
        String filePath = FilePathUtils.createFilePath(ContentDirectory.VIDEO, fileName);
        int duration = extractDuration(file);

        try {
            if (file.getSize() > CHUNK_SIZE) {
                multipartUpload(file, filePath);
            } else {
                simpleUpload(file, filePath);
            }
            return VideoFile.builder()
                .fileName(fileName).filePath(filePath).duration(duration)
                .build();

        } catch (IOException e) {
            log.error("failed to upload file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void simpleUpload(MultipartFile file, String filePath) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(filePath)
            .contentType(file.getContentType())
            .build();

        s3Client.putObject(request,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    private void multipartUpload(MultipartFile file, String filePath) throws IOException {
        String uploadId = null;
        try {
            //멀티파트 업로드 시작
            uploadId = initMultipartUpload(file, filePath);

            //청크 단위 업로드
            List<CompletedPart> completedParts = uploadParts(file, filePath, uploadId);

            //멀티파트 업로드 완료
            completeMultipartUpload(filePath, uploadId, completedParts);
        } catch (Exception e) {
            if (uploadId != null) {
                abortMultipartUpload(filePath, uploadId);
            }
            throw e;
        }
    }

    private String initMultipartUpload(MultipartFile file, String filePath) throws IOException {
        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(filePath)
            .contentType(file.getContentType())
            .build();

        return s3Client.createMultipartUpload(createRequest).uploadId();
    }

    private List<CompletedPart> uploadParts(MultipartFile file, String filePath, String uploadId)
        throws IOException {
        List<CompletedPart> completedParts = new ArrayList<>();
        byte[] bytes = file.getBytes();

        int partNumber = 1;
        int filePosition = 0;

        while (filePosition < bytes.length) {
            int partSize = Math.min(CHUNK_SIZE, bytes.length - filePosition);
            byte[] buffer = new byte[partSize];
            System.arraycopy(bytes, filePosition, buffer, 0, partSize);

            UploadPartRequest uploadRequest = UploadPartRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .build();

            String etag = s3Client.uploadPart(uploadRequest,
                RequestBody.fromBytes(buffer)).eTag();

            completedParts.add(
                CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(etag)
                    .build());

            filePosition += partSize;
            partNumber++;
        }

        return completedParts;
    }

    private void completeMultipartUpload(String filePath, String uploadId,
        List<CompletedPart> completedParts) {
        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
            .parts(completedParts)
            .build();

        CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(filePath)
            .uploadId(uploadId)
            .multipartUpload(multipartUpload)
            .build();

        s3Client.completeMultipartUpload(completeRequest);
    }

    private void abortMultipartUpload(String filePath, String uploadId) {
        try {
            AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .uploadId(uploadId)
                .build();

            s3Client.abortMultipartUpload(abortRequest);
        } catch (Exception e) {
            log.error("Failed to abort multipart upload", e);
        }
    }

    private int extractDuration(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("video", null);
            file.transferTo(tempFile);

            double durationSec = FrameGrab
                .createFrameGrab(NIOUtils.readableChannel(tempFile))
                .getVideoTrack()
                .getMeta()
                .getTotalDuration();

            tempFile.delete();

            return (int) Math.round(durationSec);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JCodecException e) {
            throw new RuntimeException(e);
        }
    }
}
