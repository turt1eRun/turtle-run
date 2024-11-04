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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trend_setter.turtlerun.global.common.BaseEntity;
import trend_setter.turtlerun.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "contents")
public class Content extends BaseEntity {

    @Id @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Setter
    @OneToOne(mappedBy = "content", cascade = CascadeType.ALL)
    private Video video;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    private ThumbnailFile thumbnail;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<DescriptionBlock> descriptionBlocks = new ArrayList<>();

    protected Content(String title, User creator, ThumbnailFile thumbnail, Video video) {
        this.title = title;
        this.creator = creator;
    }

}
