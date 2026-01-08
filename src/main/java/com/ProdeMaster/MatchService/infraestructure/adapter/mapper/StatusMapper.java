package com.ProdeMaster.MatchService.infraestructure.adapter.mapper;

import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.infraestructure.entity.StatusEntity;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public MatchStatus toDomain(StatusEntity entity) {
        // TODO: Implement later as requested.
        // Logic: StatusEntity.state coincides with MatchStatus.name()
        return null;
    }

    public StatusEntity toEntity(MatchStatus status) {
        // TODO: Implement later as requested.
        // Logic: StatusEntity.state coincides with MatchStatus.name().
        // StatusEntity.name should be the long name from API (passed separately or
        // looked up?).
        return null;
    }
}
