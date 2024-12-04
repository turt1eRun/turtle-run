package trend_setter.turtlerun.content.service;


import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileDeleteScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    private static final String JOB_NAME = "descriptionFileDeleteJob";

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteDescriptionImage() {
        try {
            jobLauncher.run(jobRegistry.getJob(JOB_NAME), createJobParameters());
        } catch (NoSuchJobException e) {
            log.error("No such batch job found: {}", JOB_NAME, e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.info("Batch job already completed: {}", JOB_NAME);
        } catch (JobExecutionAlreadyRunningException e) {
            log.info("Batch job already running: {}", JOB_NAME);
        } catch (Exception e) {
            log.error("Unexpected error during batch job execution", e);
        }
    }

    private static JobParameters createJobParameters() {
        return new JobParametersBuilder()
            .addDate("date", new Date())
            .toJobParameters();
    }
}
