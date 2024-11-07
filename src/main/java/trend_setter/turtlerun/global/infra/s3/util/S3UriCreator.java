package trend_setter.turtlerun.global.infra.s3.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class S3UriCreator {
    private static String bucket;
    private static String region;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String instanceBucket;

    @Value("${spring.cloud.aws.region.static}")
    private String instanceRegion;

    @PostConstruct
    private void init() {
        bucket = instanceBucket;
        region = instanceRegion;
    }

    public static String createUri(String filePath) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(bucket + ".s3." + region + ".amazonaws.com")
            .path(filePath)
            .build()
            .toUriString();
    }
}
