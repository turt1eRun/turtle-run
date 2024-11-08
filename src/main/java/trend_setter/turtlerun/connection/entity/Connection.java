package trend_setter.turtlerun.connection.entity;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.connection.constant.ConnectionType;
import trend_setter.turtlerun.global.common.BaseEntity;
import trend_setter.turtlerun.user.entity.User;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Table(name = "connections")
public class Connection extends BaseEntity {

    @Id @Column(name = "connection_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Connection parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Connection> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionType connectionType;

    @Column(nullable = false)
    private String content;

    protected Connection(Connection parent, ConnectionType type, String content, User writer) {
        this.parent = parent;
        this.connectionType = type;
        this.content = content;
        this.writer = writer;
    }
}
