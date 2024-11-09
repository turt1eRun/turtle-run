package trend_setter.turtlerun.content.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import trend_setter.turtlerun.content.constant.BlockType;
import trend_setter.turtlerun.content.dto.GetContentListResponse;
import trend_setter.turtlerun.content.entity.Content;
import trend_setter.turtlerun.content.entity.DescriptionBlock;
import trend_setter.turtlerun.content.entity.ThumbnailFile;
import trend_setter.turtlerun.content.entity.VideoFile;
import trend_setter.turtlerun.global.config.QuerydslConfig;
import trend_setter.turtlerun.user.constant.Role;
import trend_setter.turtlerun.user.entity.User;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class ContentRepositoryCustomImplTest {

    @Autowired
    private EntityManager em;

    private ContentRepositoryCustomImpl contentRepository;
    private User creator;

    @BeforeEach
    void setUp() {
        contentRepository = new ContentRepositoryCustomImpl(new JPAQueryFactory(em));

        creator = User.builder().nickname("creator").password("password").email("email")
            .role(Role.RABBIT).build();
        em.persist(creator);
        em.flush();
    }

    @Test
    void 제목으로_컨텐츠_검색시_정상적으로_결과가_반환된다() {

        // given
        createContent(creator, "테스트 제목", List.of("일반 설명", "두번째 설명"));
        createContent(creator, "일반 제목", List.of("테스트 설명", "추가 설명"));
        createContent(creator, "관계없는 내용", List.of("관계없는 설명"));
        flushAndClear();

        // when
        Page<GetContentListResponse> result = contentRepository.findContentsByKeyword("테스트",
            PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("title").contains("테스트 제목", "일반 제목");
    }

    @Test
    void 설명_내용으로_컨텐츠_검색_시_정상적으로_결과가_반환된다() {

        // given
        createContent(creator, "제목1", List.of("일반 설명", "두번째 설명"));
        createContent(creator, "제목2", List.of("테스트 설명", "추가 설명"));
        createContent(creator, "제목3", List.of("테스트 설명", "관계없는 설명"));
        flushAndClear();

        // when
        Page<GetContentListResponse> result = contentRepository.findContentsByKeyword(
            "테스트",
            PageRequest.of(0, 10)
        );

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
            .extracting("title")
            .contains("제목3", "제목2");
    }

    @Test
    void 검색어가_없을_경우_모든_컨텐츠가_반환된다() {
        // given
        createContent(creator, "제목1", List.of("일반 설명", "두번째 설명"));
        createContent(creator, "제목2", List.of("테스트 설명", "추가 설명"));
        createContent(creator, "제목3", List.of("테스트 설명", "관계없는 설명"));
        flushAndClear();

        // when
        Page<GetContentListResponse> result = contentRepository.findContentsByKeyword(
            null,
            PageRequest.of(0, 10)
        );

        // then
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    void 페이징이_정상적으로_동작한다() {
        // given
        for (int i = 1; i <= 15; i++) {
            createContent(creator, "제목" + i, List.of("설명" + i));
        }

        // when
        Page<GetContentListResponse> result = contentRepository.findContentsByKeyword(
            null,
            PageRequest.of(1, 10)
        );

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    private void createContent(User user, String title, List<String> descriptions) {
        ThumbnailFile newThumbnail = ThumbnailFile.testBuilder()
            .fileName("filename_" + title)
            .filePath("thumbnail_" + title + ".jpg")
            .build();
        em.persist(newThumbnail);

        VideoFile newVideo = VideoFile.testBuilder()
            .duration(180)
            .build();
        em.persist(newVideo);

        Content content = Content.testBuilder()
            .title(title)
            .creator(user)
            .video(newVideo)
            .thumbnail(newThumbnail)
            .build();

        em.persist(content);

        descriptions.forEach(desc -> {
            DescriptionBlock block = DescriptionBlock.testBuilder().text(desc).type(BlockType.TEXT)
                .build();
            content.addDescriptionBlock(block);
            em.persist(block);
        });
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }


}