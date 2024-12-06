package trend_setter.turtlerun.global.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
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
import trend_setter.turtlerun.content.repository.DescriptionFileRepository;
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

    @Autowired
    private Job deleteS3descriptionFileJob;

    @MockBean
    private S3ImageUploader s3ImageUploader;

    @BeforeEach
    void setUp() {
        // 테스트용 job 설정
        jobLauncherTestUtils.setJob(deleteS3descriptionFileJob);
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void 만료_시간이_지난_파일은_삭제되어야_한다() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        DescriptionFile boundaryFile = createDescriptionFile(
            now.minusHours(BatchConfig.EXPIRATION_HOURS), "boundary");
        DescriptionFile validFile = createDescriptionFile(
            now.minusHours(BatchConfig.EXPIRATION_HOURS - 1), "valid");
        DescriptionFile expiredFile = createDescriptionFile(
            now.minusHours(BatchConfig.EXPIRATION_HOURS + 1), "expired");

        descriptionFileRepository.saveAll(List.of(boundaryFile, validFile, expiredFile));

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        //만료시간이 지난것만 delete 메서드 동작
        verify(s3ImageUploader, never()).delete(boundaryFile.getFilePath());
        verify(s3ImageUploader, never()).delete(validFile.getFilePath());
        verify(s3ImageUploader).delete(expiredFile.getFilePath());

        //만료시간이 지난 파일 삭제
        assertTrue(descriptionFileRepository.existsById(boundaryFile.getId()));
        assertTrue(descriptionFileRepository.existsById(validFile.getId()));
        assertFalse(descriptionFileRepository.existsById(expiredFile.getId()));
    }

    private static DescriptionFile createDescriptionFile(LocalDateTime deletedAt, String filePath) {
        return DescriptionFile.testBuilder()
            .filePath("test/" + filePath)
            .deletedAt(deletedAt)
            .build();
    }
}