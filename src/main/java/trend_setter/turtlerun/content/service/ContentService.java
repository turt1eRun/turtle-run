package trend_setter.turtlerun.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trend_setter.turtlerun.content.dto.CreateContentRequest;
import trend_setter.turtlerun.content.entity.Content;
import trend_setter.turtlerun.content.repository.ContentRepository;
import trend_setter.turtlerun.content.repository.DescriptionFileRepository;
import trend_setter.turtlerun.content.repository.ThumbnailFileRepository;
import trend_setter.turtlerun.content.repository.VideoFileRepository;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;
import trend_setter.turtlerun.user.entity.User;
import trend_setter.turtlerun.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final VideoFileRepository videoFileRepository;
    private final ThumbnailFileRepository thumbnailFileRepository;
    private final UserRepository userRepository;
    private final DescriptionFileRepository descriptionFileRepository;

    @Transactional
    public void createContent(UserDetails userDetails, CreateContentRequest request) {
        validateUserAuthority(userDetails);
        validateRequest(request);
        User creator = findUser(userDetails);
        Content content = request.toEntity(creator);
        contentRepository.save(content);
    }

    private void validateUserAuthority(UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains("ROLE_RABBIT")) {
            throw new ContentException(ContentErrorCode.UNAUTHORIZED_CREATOR);
        }
    }

    private void validateRequest(CreateContentRequest request) {
        videoFileRepository.findById(request.videoFileId())
            .orElseThrow(() -> new ContentException(ContentErrorCode.VIDEO_FILE_NOT_FOUND));

        thumbnailFileRepository.findById(request.thumbnailFileId())
            .orElseThrow(() -> new ContentException(ContentErrorCode.THUMBNAIL_FILE_NOT_FOUND));

        request.createBlockRequests().stream()
            .filter(blockRequest -> blockRequest.descFileId() != null)
            .forEach(blockRequest ->
                descriptionFileRepository.findById(blockRequest.descFileId()).orElseThrow(
                    () -> new ContentException(ContentErrorCode.DESCRIPTION_FILE_NOT_FOUND)));
    }

    private User findUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(RuntimeException::new);
    }
}


