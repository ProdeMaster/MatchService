package com.ProdeMaster.MatchService.application.port.in.web;

import com.ProdeMaster.MatchService.domain.model.MatchModel;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface MatchService {
    // Operaciones básicas
    MatchModel createMatch(MatchModel match);
    Optional<MatchModel> getMatch(Long matchId);
    List<MatchModel> getAllMatches();

    // Operaciones de actualización
    MatchModel updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore);
    MatchModel updateMatchStatus(Long matchId, MatchStatus newStatus);
    MatchModel confirmMatchResult(Long matchId);

    // Consultas específicas
    List<MatchModel> getMatchesByLeague(String league);
    List<MatchModel> getMatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MatchModel> getMatchesByStatus(MatchStatus status);
    List<MatchModel> getMatchesByTeam(String teamName);

    // Operaciones de validación
    boolean isMatchExist(Long matchId);
    boolean canUpdateMatch(Long matchId);
}