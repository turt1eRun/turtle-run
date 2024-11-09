package trend_setter.turtlerun.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import trend_setter.turtlerun.content.dto.GetContentListResponse;

public interface ContentRepositoryCustom {
    Page<GetContentListResponse> findContentsByKeyword(String query, Pageable pageable);
}
