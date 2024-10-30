package trend_setter.turtlerun.global.util;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Component;
import trend_setter.turtlerun.global.common.StorageDirectory;

@Component
public class FilePathUtils {

    public static String createFilePath(StorageDirectory directory, String fileName) {
        LocalDate now = LocalDate.now();
        return String.format("%s/%d/%02d/%s",
            directory.getDirectory(),
            now.getYear(),
            now.getMonthValue(),
            fileName);
    }

    public static String createFileName() {
        return UUID.randomUUID().toString();
    }
}
