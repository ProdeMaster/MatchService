package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.infraestructure.entity.MatchEntity;
import com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.MatchMapper;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaMatchRepository implements MatchRepository {
    private final SpringDataMatchRepository jpaRepository;
    private final MatchMapper mapper;

    public JpaMatchRepository(SpringDataMatchRepository jpaRepository, MatchMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Match save(Match match) {
        MatchEntity savedEntity = jpaRepository.save(mapper.toEntity(match));
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Match> findById(Long matchId) {
        return jpaRepository.findById(matchId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Match> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long matchId) {
        jpaRepository.deleteById(matchId);
    }

    @Override
    public List<Match> findByLeague(String league) {
        // Note: This now searches by league name in the entity's league relationship
        // For now, we'll return all matches and filter in memory
        // TODO: Implement proper league name to ID mapping or add query method
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .filter(match -> match.getLeague() != null && match.getLeague().equals(league))
                .toList();
    }

    @Override
    public List<Match> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByStartingAtBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Match> findByStatus(MatchStatus status) {
        // Convert MatchStatus to stateId and query
        Integer stateId = mapMatchStatusToStateId(status);
        return jpaRepository.findByStateId(stateId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Match> findByTeam(String teamName) {
        return jpaRepository.findByNameContaining(teamName).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long matchId) {
        return jpaRepository.existsById(matchId);
    }

    @Override
    public void updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore) {
        // Load the domain match, update it, and save back
        Match match = findById(matchId).orElseThrow();
        match.updateScore(homeTeamScore, awayTeamScore);
        save(match);
    }

    @Override
    public void updateMatchStatus(Long matchId, MatchStatus status) {
        // Update status directly on entity (bypasses domain validation)
        // Note: This should ideally use domain methods for proper state transitions
        MatchEntity entity = jpaRepository.findById(matchId).orElseThrow();
        entity.setStateId(mapMatchStatusToStateId(status));
        jpaRepository.save(entity);
    }

    @Override
    public void confirmMatchResult(Long matchId) {
        // Load the domain match, confirm result, and save back
        Match match = findById(matchId).orElseThrow();
        match.confirmResult();
        save(match);
    }

    /**
     * Helper method to map MatchStatus to state ID.
     * This duplicates logic from MatchMapper - consider refactoring to share.
     */
    private Integer mapMatchStatusToStateId(MatchStatus status) {
        if (status == null) {
            return 1;
        }

        return switch (status) {
            case PENDING -> 0;
            case TBA -> 1;
            case NS -> 1;
            case INPLAY_1ST_HALF -> 2;
            case HT -> 3;
            case INPLAY_2ND_HALF -> 4;
            case FT -> 5;
            case INPLAY_ET -> 6;
            case EXTRA_TIME_BREAK -> 7;
            case INPLAY_ET_2ND_HALF -> 8;
            case AET -> 9;
            case INPLAY_PENALTIES -> 10;
            case PEN_BREAK -> 11;
            case FT_PEN -> 12;
            case DELAYED -> 13;
            case POSTPONED -> 14;
            case SUSPENDED -> 15;
            case INTERRUPTED -> 16;
            case AWARDED -> 17;
            case WO -> 18;
            case CANCELLED -> 19;
            case ABANDONED -> 20;
            case DELETED -> 21;
            case AWAITING_UPDATES -> 22;
            default -> 1; // Default to NS state
        };
    }
}
