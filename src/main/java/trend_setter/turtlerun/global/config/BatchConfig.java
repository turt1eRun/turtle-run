package trend_setter.turtlerun.global.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.PlatformTransactionManager;
import trend_setter.turtlerun.content.repository.DescriptionFileRepository;
import trend_setter.turtlerun.content.repository.ThumbnailFileRepository;
import trend_setter.turtlerun.content.repository.VideoFileRepository;
import trend_setter.turtlerun.global.error.exception.FileException;
import trend_setter.turtlerun.global.infra.s3.entity.S3Deletable;
import trend_setter.turtlerun.global.infra.s3.service.S3SimpleUploader;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final S3SimpleUploader s3SimpleUploader;
    private final DescriptionFileRepository descriptionFileRepository;
    private final VideoFileRepository videoFileRepository;
    private final ThumbnailFileRepository thumbnailFileRepository;
    private final PlatformTransactionManager platformTransactionManager;
    public static final int EXPIRATION_HOURS = 24;


    @Bean
    public Job deleteS3FileJob() {

        return new JobBuilder("deleteS3FileJob", jobRepository)
            .start(createDeleteStep("description", descriptionFileRepository))
            .next(createDeleteStep("video", videoFileRepository))
            .next(createDeleteStep("thumbnail", thumbnailFileRepository))
            .build();
    }


    private  <T extends S3Deletable> Step createDeleteStep(String prefix, JpaRepository<T, Long> repository
    ) {
        return new StepBuilder(prefix + "FileDeleteStep", jobRepository)
            .<T, T>chunk(100, platformTransactionManager)
            .reader(createReader(prefix, repository))
            .processor(createProcessor())
            .writer(createWriter(repository))
            .build();
    }


    private  <T extends S3Deletable> RepositoryItemReader<T> createReader(
        String prefix,
        JpaRepository<T, Long> repository
    ) {
        return new RepositoryItemReaderBuilder<T>()
            .name(prefix + "FileReader")
            .repository(repository)
            .methodName("findByDeletedAtBefore")
            .pageSize(100)
            .arguments(List.of(LocalDateTime.now().minusHours(EXPIRATION_HOURS)))
            .sorts(Map.of("id", Direction.ASC))
            .build();
    }


    private  <T extends S3Deletable> ItemProcessor<T, T> createProcessor() {
        return file -> {
            try {
                s3SimpleUploader.delete(file.getFilePath());
                return file;
            } catch (FileException e) {
                log.error("Failed to delete S3 file {}", file.getFilePath(), e);
                return null;
            }
        };
    }


    private  <T extends S3Deletable> RepositoryItemWriter<T> createWriter(
        JpaRepository<T, Long> repository) {
        return new RepositoryItemWriterBuilder<T>()
            .repository(repository)
            .methodName("delete")
            .build();
    }
}

