package com.ProdeMaster.MatchService.application.port.in.web;

import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchService {
    // Basic operations
    Match createMatch(Match match);

    Optional<Match> getMatch(Long matchId);

    List<Match> getAllMatches();

    // Update operations
    Match updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore);

    Match updateMatchStatus(Long matchId, MatchStatus newStatus);

    Match confirmMatchResult(Long matchId);

    // Specific queries
    List<Match> getMatchesByLeague(String league);

    List<Match> getMatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Match> getMatchesByStatus(MatchStatus status);

    List<Match> getMatchesByTeam(String teamName);

    // Validation operations
    boolean isMatchExist(Long matchId);

    boolean canUpdateMatch(Long matchId);
}