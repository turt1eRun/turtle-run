package trend_setter.turtlerun.global.infra.s3.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import trend_setter.turtlerun.global.error.code.FileErrorCode;
import trend_setter.turtlerun.global.error.exception.FileException;

@Service
@RequiredArgsConstructor
public class S3VideoUploader {

    private static final int CHUNK_SIZE = 5 * 1024 * 1024;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public void upload(MultipartFile file, String filePath, Map<String, String> metadata) {
        try {
            if (file.getSize() > CHUNK_SIZE) {
                multipartUpload(file, filePath, metadata);
            } else {
                simpleUpload(file, filePath, metadata);
            }
        } catch (IOException | S3Exception e) {
            throw new FileException(FileErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    private void simpleUpload(MultipartFile file, String filePath, Map<String, String> metadata)
        throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(filePath)
            .contentType(file.getContentType())
            .metadata(metadata)
            .build();

        s3Client.putObject(request,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    protected void multipartUpload(MultipartFile file, String filePath,
        Map<String, String> metadata) throws IOException {
        String uploadId = null;
        try {
            //멀티파트 업로드 시작
            uploadId = initMultipartUpload(file, filePath, metadata);

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

    protected String initMultipartUpload(MultipartFile file, String filePath,
        Map<String, String> metadata) throws IOException {
        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
            .bucket(bucket)
            .key(filePath)
            .contentType(file.getContentType())
            .metadata(metadata)
            .build();

        return s3Client.createMultipartUpload(createRequest).uploadId();
    }

    protected List<CompletedPart> uploadParts(MultipartFile file, String filePath, String uploadId)
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

    protected void completeMultipartUpload(String filePath, String uploadId,
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

    protected void abortMultipartUpload(String filePath, String uploadId) {
        try {
            AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .uploadId(uploadId)
                .build();

            s3Client.abortMultipartUpload(abortRequest);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FAILED_TO_ABORT);
        }
    }

}