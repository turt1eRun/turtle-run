package trend_setter.turtlerun.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import trend_setter.turtlerun.content.dto.GetContentListResponse;

@Repository
public interface ContentRepositoryCustom {
    Page<GetContentListResponse> findContentsByKeyword(String query, Pageable pageable);
}
