package trend_setter.turtlerun.content.dto;

import trend_setter.turtlerun.content.entity.DescriptionFile;
import trend_setter.turtlerun.content.entity.ThumbnailFile;
import trend_setter.turtlerun.content.entity.VideoFile;
import trend_setter.turtlerun.global.infra.s3.util.S3UriCreator;
/**
 * 파일 업로드 응답
 *
 * @param id        엔티티 연관관계를 위한 식별자
 * @param uploadUrl 파일 미리보기를 위한 S3 URL
 */
public record GetFileUploadResponse(Long id, String uploadUrl) {

    public GetFileUploadResponse(VideoFile videoFile) {
        this(videoFile.getId(), S3UriCreator.createUri(videoFile.getFilePath()));
    }

    public GetFileUploadResponse(ThumbnailFile thumbnailFile) {
        this(thumbnailFile.getId(), S3UriCreator.createUri(thumbnailFile.getFilePath()));
    }

    public GetFileUploadResponse(DescriptionFile descriptionFile) {
        this(descriptionFile.getId(), S3UriCreator.createUri(descriptionFile.getFilePath()));
    }
}