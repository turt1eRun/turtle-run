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
import trend_setter.turtlerun.content.constant.BlockType;
import trend_setter.turtlerun.content.dto.CreateBlockRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "description_blocks")
public class DescriptionBlock {

    @Id @Column(name = "description_block_id")
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

    public DescriptionBlock(CreateBlockRequest request, Content content) {
        if(request.text()!=null){
            this.type = BlockType.TEXT;
            this.text = request.text();
        }
        else {
            this.type = BlockType.IMAGE;
            this.descriptionFile = new DescriptionFile(request.descFileId());
        }
        this.orderNum = request.orderNum();
        this.content = content;
    }
}
