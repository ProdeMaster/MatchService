package com.ProdeMaster.MatchService.application.port.out.db;

import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    // Basic CRUD operations
    Match save(Match match);

    Optional<Match> findById(Long matchId);

    List<Match> findAll();

    void deleteById(Long matchId);

    // Domain specific queries
    List<Match> findByLeague(String league);

    List<Match> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Match> findByStatus(MatchStatus status);

    List<Match> findByTeam(String teamName);

    boolean existsById(Long matchId);

    // Specific update operations
    void updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore);

    void updateMatchStatus(Long matchId, MatchStatus status);

    void confirmMatchResult(Long matchId);
}