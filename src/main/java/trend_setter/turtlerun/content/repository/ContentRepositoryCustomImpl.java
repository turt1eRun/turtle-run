package trend_setter.turtlerun.content.repository;

import static org.springframework.util.StringUtils.hasText;
import static trend_setter.turtlerun.content.entity.QDescriptionBlock.descriptionBlock;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import trend_setter.turtlerun.content.dto.GetContentListResponse;
import trend_setter.turtlerun.content.dto.QGetContentListResponse;
import trend_setter.turtlerun.content.entity.QContent;

@Repository
@RequiredArgsConstructor
public class ContentRepositoryCustomImpl implements ContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QContent content = QContent.content;

    @Override
    public Page<GetContentListResponse> findContentsByKeyword(String keyword, Pageable pageable) {

        List<GetContentListResponse> contents = queryFactory
            .select(new QGetContentListResponse(
                content.id,
                content.title,
                content.creator.nickname,
                content.views,
                content.video.duration,
                content.thumbnail,
                content.createdAt
            ))
            .from(content)
            .join(content.creator)
            .join(content.video)
            .join(content.thumbnail)
            .where(searchCondition(keyword))
            .orderBy(content.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(content.count())
            .from(content)
            .where(searchCondition(keyword));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchFirst);

    }

    private BooleanExpression searchCondition(String keyword) {
        return hasText(keyword) ?
            content.title.contains(keyword)
                .or(
                    JPAExpressions
                        .selectOne()
                        .from(descriptionBlock)
                        .where(descriptionBlock.content.eq(content)
                            .and(descriptionBlock.text.contains(keyword)))
                        .exists()
                ) : null;
    }

}
