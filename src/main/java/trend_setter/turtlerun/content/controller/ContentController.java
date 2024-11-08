package trend_setter.turtlerun.content.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import trend_setter.turtlerun.content.dto.CreateContentRequest;
import trend_setter.turtlerun.content.dto.GetFileUploadResponse;
import trend_setter.turtlerun.content.service.ContentService;
import trend_setter.turtlerun.content.service.DescriptionService;
import trend_setter.turtlerun.content.service.ThumbnailService;
import trend_setter.turtlerun.content.service.VideoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contents")
public class ContentController {

    private final ContentService contentService;
    private final VideoService videoService;
    private final ThumbnailService thumbnailService;
    private final DescriptionService descriptionService;

    @PostMapping("/videos")
    public GetFileUploadResponse uploadVideo(@RequestPart("video") MultipartFile video) {
        return videoService.uploadVideo(video);
    }

    @PostMapping("/thumbnails")
    public GetFileUploadResponse uploadThumbnail(@RequestPart("thumbnail") MultipartFile thumbnail) {
        return thumbnailService.uploadThumbnail(thumbnail);
    }

    @PostMapping("/block-images")
    public GetFileUploadResponse uploadBlockImage(@RequestPart("block-image") MultipartFile blockImage) {
        return descriptionService.uploadDescriptionImage(blockImage);
    }

    @PostMapping
    public void createContent(
        @AuthenticationPrincipal UserDetails user,
        @RequestBody @Valid CreateContentRequest request
    ) {
        contentService.createContent(user, request);
    }
}
