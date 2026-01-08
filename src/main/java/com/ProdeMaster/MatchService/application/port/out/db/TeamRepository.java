package com.ProdeMaster.MatchService.application.port.out.db;

import com.ProdeMaster.MatchService.domain.projection.TeamSnapshot;

import java.util.Optional;

public interface TeamRepository {
    Optional<TeamSnapshot> findByName(String homeTeam);
    Optional<TeamSnapshot> findById(Long TeamId);
}
