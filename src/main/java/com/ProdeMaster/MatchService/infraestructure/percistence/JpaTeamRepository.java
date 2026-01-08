package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.application.port.out.db.TeamRepository;
import com.ProdeMaster.MatchService.domain.projection.TeamSnapshot;
import com.ProdeMaster.MatchService.infraestructure.adapter.mapper.TeamMapper;
import com.ProdeMaster.MatchService.infraestructure.entity.TeamEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaTeamRepository implements TeamRepository {
    private final SpringDataTeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public JpaTeamRepository(SpringDataTeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public Optional<TeamSnapshot> findByName(String homeTeam) {
        TeamEntity teamEntity = teamRepository.findByName(homeTeam).get();
        return teamMapper.toTeamSnapshot(teamEntity);
    }

    @Override
    public Optional<TeamSnapshot> findById(Long TeamId) {
        TeamEntity teamEntity = teamRepository.findById(TeamId).get();
        return teamMapper.toTeamSnapshot(teamEntity);
    }
}