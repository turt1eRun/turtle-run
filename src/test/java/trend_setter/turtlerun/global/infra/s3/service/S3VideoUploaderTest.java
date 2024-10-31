package trend_setter.turtlerun.global.infra.s3.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import trend_setter.turtlerun.global.error.exception.FileException;

@ExtendWith(MockitoExtension.class)
class S3VideoUploaderTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3VideoUploader s3VideoUploader;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3VideoUploader, "bucket", "test-bucket");
    }


    @ParameterizedTest
    @ValueSource(ints = {1024 * 1024 , 5 * 1024 * 1024})
    void 청크_사이즈_이하의_파일은_단순_업로드를_수행한다(int byteSize) {
        // Given
        MultipartFile file = new MockMultipartFile(
            "test.mp4", "test.mp4",
            "video/mp4", new byte[byteSize]
        );
        String key = "videos/test.mp4";
        Map<String, String> metadata = Map.of("duration", "10");

        // When
        s3VideoUploader.upload(file, key, metadata);

        // Then
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void 청크_사이즈를_초과하는_파일은_멀티파트_업로드를_수행한다() {
        // Given
        MultipartFile file = new MockMultipartFile(
            "test.mp4", "test.mp4",
            "video/mp4", new byte[6 * 1024 * 1024]
        );
        String key = "videos/test.mp4";
        Map<String, String> metadata = Map.of("duration", "10");

        when(s3Client.createMultipartUpload(any(CreateMultipartUploadRequest.class)))
            .thenReturn(CreateMultipartUploadResponse.builder()
                .uploadId("test-upload-id")
                .build());

        when(s3Client.uploadPart(any(UploadPartRequest.class), any(RequestBody.class)))
            .thenReturn(UploadPartResponse.builder()
                .eTag("test-etag")
                .build());

        // When
        s3VideoUploader.upload(file, key, metadata);

        // Then
        verify(s3Client).createMultipartUpload(any(CreateMultipartUploadRequest.class));
        verify(s3Client, atLeastOnce()).uploadPart(any(UploadPartRequest.class), any(RequestBody.class));
        verify(s3Client).completeMultipartUpload(any(CompleteMultipartUploadRequest.class));
    }

    @Test
    void S3_업로드_실패시_FileException을_던진다(){
        // Given
        MultipartFile file = new MockMultipartFile(
            "test.mp4", "test.mp4",
            "video/mp4", "test".getBytes()
        );
        String key = "videos/test.mp4";
        Map<String, String> metadata = Map.of("duration", "10");

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenThrow(S3Exception.builder().message("Upload failed").build());

        // When & Then
        assertThrows(FileException.class, () ->
            s3VideoUploader.upload(file, key, metadata)
        );
    }
}