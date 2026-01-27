package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.application.port.out.db.TeamRepository;
import com.ProdeMaster.MatchService.domain.projection.TeamSnapshot;
import com.ProdeMaster.MatchService.infraestructure.adapter.mapper.TeamMapper;
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
        return teamRepository.findByName(homeTeam)
                .flatMap(teamMapper::toTeamSnapshot);
    }

    @Override
    public Optional<TeamSnapshot> findById(Long teamId) {
        if (teamId == null) {
            return Optional.empty();
        }
        return teamRepository.findById(teamId)
                .flatMap(teamMapper::toTeamSnapshot);
    }
}