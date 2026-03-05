package com.ProdeMaster.MatchService.infraestructure.adapter.mapper;

import com.ProdeMaster.MatchService.domain.projection.TeamSnapshot;
import com.ProdeMaster.MatchService.infraestructure.entity.TeamEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TeamMapper {

    public Optional<TeamSnapshot> toTeamSnapshot(TeamEntity teamEntity) {
        if (teamEntity == null) {
            return Optional.empty();
        }
        return Optional.of(new TeamSnapshot(teamEntity.getId(), teamEntity.getName()));
    }
}
