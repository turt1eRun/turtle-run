package trend_setter.turtlerun.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "description_blocks")
public class DescriptionBlock {

    @Id
    @Column(name = "description_block_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BlockType type;

    @Column(columnDefinition = "TEXT")
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "description_file_id")
    private DescriptionFile descriptionFile;

    private int orderNum;

    // TEXT 블럭용 생성자
    protected DescriptionBlock(Content content, String text, int orderNum) {
        this.content = content;
        this.type = BlockType.TEXT;
        this.text = text;
        this.orderNum = orderNum;
    }

    // IMAGE 블럭용 생성자
    protected DescriptionBlock(Content content, DescriptionFile file, int orderNum) {
        this.content = content;
        this.type = BlockType.IMAGE;
        this.descriptionFile = file;
        this.orderNum = orderNum;
    }
}
