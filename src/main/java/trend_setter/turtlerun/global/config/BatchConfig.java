package trend_setter.turtlerun.global.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import trend_setter.turtlerun.content.entity.DescriptionFile;
import trend_setter.turtlerun.content.repository.DescriptionFileRepository;
import trend_setter.turtlerun.global.error.exception.FileException;
import trend_setter.turtlerun.global.infra.s3.service.S3ImageUploader;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final S3ImageUploader s3ImageUploader;
    private final DescriptionFileRepository descriptionFileRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job deleteS3descriptionFileJob() {

        return new JobBuilder("descriptionFileDeleteJob", jobRepository)
            .start(deleteS3descriptionFileStep())
            .build();
    }

    @Bean
    public Step deleteS3descriptionFileStep() {
        return new StepBuilder("deleteS3descriptionFileStep", jobRepository)
            .<DescriptionFile, DescriptionFile>chunk(100, platformTransactionManager)
            .reader(descriptionFileReader())
            .processor(descriptionFileProcessor())
            .writer(descriptionFileWriter())
            .build();
    }

    @Bean
    public RepositoryItemReader<DescriptionFile> descriptionFileReader() {
        return new RepositoryItemReaderBuilder<DescriptionFile>()
            .name("descriptionFileReader")
            .repository(descriptionFileRepository)
            .methodName("findByDeletedAtBefore")
            .pageSize(100)
            .arguments(List.of(LocalDateTime.now().minusHours(24)))
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public ItemProcessor<DescriptionFile, DescriptionFile> descriptionFileProcessor() {
        return file -> {
            try {
                s3ImageUploader.delete(file.getFilePath());
                return file;
            } catch (FileException e) {
                log.error("Failed to delete S3 file {}", file.getFilePath(), e);
                return null;
            }
        };
    }

    @Bean
    public RepositoryItemWriter<DescriptionFile> descriptionFileWriter() {
        return new RepositoryItemWriterBuilder<DescriptionFile>()
            .repository(descriptionFileRepository)
            .methodName("delete")
            .build();
    }
}

