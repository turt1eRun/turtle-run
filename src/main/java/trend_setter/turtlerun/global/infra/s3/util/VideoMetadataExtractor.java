package trend_setter.turtlerun.global.infra.s3.util;

import java.io.File;
import java.io.IOException;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.springframework.stereotype.Component;

@Component
public class VideoMetadataExtractor {

    public double getVideoDuration(File file) throws IOException, JCodecException {
        return  FrameGrab
            .createFrameGrab(NIOUtils.readableChannel(file))
            .getVideoTrack()
            .getMeta()
            .getTotalDuration();
    }
}