package trend_setter.turtlerun.global.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import trend_setter.turtlerun.content.entity.DescriptionFile;
import trend_setter.turtlerun.content.entity.ThumbnailFile;
import trend_setter.turtlerun.content.entity.VideoFile;
import trend_setter.turtlerun.content.repository.DescriptionFileRepository;
import trend_setter.turtlerun.content.repository.ThumbnailFileRepository;
import trend_setter.turtlerun.content.repository.VideoFileRepository;
import trend_setter.turtlerun.global.infra.s3.service.S3ImageUploader;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest
class BatchConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private DescriptionFileRepository descriptionFileRepository;

    @MockBean
    private S3ImageUploader s3ImageUploader;

    @Autowired
    private Job deleteS3FileJob;
    @Autowired
    private VideoFileRepository videoFileRepository;
    @Autowired
    private ThumbnailFileRepository thumbnailFileRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 job 설정
        jobLauncherTestUtils.setJob(deleteS3FileJob);
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void 만료된_파일은_삭제되어야_한다() throws Exception {
        //given
        LocalDateTime expiredTime = LocalDateTime.now().minusHours(BatchConfig.EXPIRATION_HOURS + 1);
        DescriptionFile expiredFile = createDescriptionFile(expiredTime, "expired");
        descriptionFileRepository.save(expiredFile);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        verify(s3ImageUploader).delete(expiredFile.getFilePath());
        assertFalse(descriptionFileRepository.existsById(expiredFile.getId()));
    }

    @Test
    void 만료되지_않은_파일은_삭제되지_않아야_한다() throws Exception {
        //given
        LocalDateTime validTime = LocalDateTime.now().minusHours(BatchConfig.EXPIRATION_HOURS - 1);
        VideoFile validFile = createVideoFile(validTime, "valid");
        videoFileRepository.save(validFile);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        verify(s3ImageUploader, never()).delete(validFile.getFilePath());
        assertTrue(videoFileRepository.existsById(validFile.getId()));
    }

    @Test
    void 경계값의_파일은_삭제되지_않아야_한다()throws Exception {
        //given
        LocalDateTime boundaryTime = LocalDateTime.now().minusHours(BatchConfig.EXPIRATION_HOURS);
        ThumbnailFile boundaryFile = createThumbnailFile(boundaryTime, "boundary");
        thumbnailFileRepository.save(boundaryFile);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        verify(s3ImageUploader, never()).delete(boundaryFile.getFilePath());
        assertTrue(thumbnailFileRepository.existsById(boundaryFile.getId()));
    }


    private DescriptionFile createDescriptionFile(LocalDateTime deletedAt, String filePath) {
        return DescriptionFile.testBuilder()
            .filePath("test/descriptions/" + filePath)
            .deletedAt(deletedAt)
            .build();
    }


    private VideoFile createVideoFile(LocalDateTime deletedAt, String filePath) {
        return VideoFile.testBuilder()
            .filePath("test/videos/" + filePath)
            .deletedAt(deletedAt)
            .build();
    }

    private ThumbnailFile createThumbnailFile(LocalDateTime deletedAt, String filePath) {
        return ThumbnailFile.testBuilder()
            .filePath("test/thumbnails/" + filePath)
            .deletedAt(deletedAt)
            .build();
    }
}