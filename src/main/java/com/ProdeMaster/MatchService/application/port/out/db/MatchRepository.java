package com.ProdeMaster.MatchService.application.port.out.db;

import com.ProdeMaster.MatchService.domain.model.MatchModel;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface MatchRepository {
    // Operaciones básicas CRUD
    MatchModel save(MatchModel match);
    Optional<MatchModel> findById(Long matchId);
    List<MatchModel> findAll();
    void deleteById(Long matchId);

    // Consultas específicas del dominio
    List<MatchModel> findByLeague(String league);
    List<MatchModel> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MatchModel> findByStatus(MatchStatus status);
    List<MatchModel> findByTeam(String teamName);
    boolean existsById(Long matchId);

    // Operaciones de actualización específicas
    void updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore);
    void updateMatchStatus(Long matchId, MatchStatus status);
    void confirmMatchResult(Long matchId);
}