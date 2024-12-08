package trend_setter.turtlerun.content.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import trend_setter.turtlerun.content.constant.BlockType;
import trend_setter.turtlerun.content.dto.BlockRequest;
import trend_setter.turtlerun.content.dto.CreateContentRequest;
import trend_setter.turtlerun.content.dto.ModifyContentRequest;
import trend_setter.turtlerun.global.common.BaseEntity;
import trend_setter.turtlerun.global.error.code.ContentErrorCode;
import trend_setter.turtlerun.global.error.exception.ContentException;
import trend_setter.turtlerun.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "contents")
public class Content extends BaseEntity {

    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private VideoFile video;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id", nullable = false)
    private ThumbnailFile thumbnail;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DescriptionBlock> descriptionBlocks = new ArrayList<>();

    private long views;

    public Content(CreateContentRequest request, User user) {
        this.title = request.title();
        this.creator = user;
        this.video = new VideoFile(request.videoFileId());
        this.thumbnail = new ThumbnailFile(request.thumbnailFileId());
        this.descriptionBlocks = request.blockRequests()
            .stream()
            .map(blockRequest -> blockRequest.toEntity(this))
            .toList();
    }

    public void addDescriptionBlock(DescriptionBlock block) {
        descriptionBlocks.add(block);
        block.setContent(this);
    }

    public void validateCreatorPermission(UserDetails user) {
        if (this.creator.getEmail().equals(user.getUsername())){
            throw new ContentException(ContentErrorCode.UNAUTHORIZED_PERMISSION);
        }
    }

    public void modifyContentInfo(ModifyContentRequest request) {
        this.title = request.title();
        //재사용되는 파일 id 찾기
        List<Long> reusedImageFileIds = request.blockRequests()
            .stream().filter(req -> req.text() == null)
            .map(BlockRequest::descFileId)
            .toList();
        //이미지 파일 soft delete
        this.descriptionBlocks.stream()
            .filter(block -> block.getType().equals(BlockType.IMAGE))
            .filter(block -> !reusedImageFileIds.contains(block.getId()))
            .forEach(block -> block.getDescriptionFile().delete());
        //연관관계 제거
        this.descriptionBlocks.clear();
        //변경내용 적용
        this.descriptionBlocks = request.blockRequests()
            .stream()
            .map(modifyBlockRequest -> modifyBlockRequest.toEntity(this))
            .toList();
    }

    @Builder(builderMethodName = "testBuilder")
    private Content(String title, User creator, VideoFile video, ThumbnailFile thumbnail,
        List<DescriptionBlock> descriptionBlocks, long views) {
        this.title = title;
        this.creator = creator;
        this.video = video;
        this.thumbnail = thumbnail;
        this.views = views;
    }
}
