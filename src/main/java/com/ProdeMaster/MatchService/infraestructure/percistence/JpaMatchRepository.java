package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.infraestructure.entity.MatchEntity;
import com.ProdeMaster.MatchService.infraestructure.adapter.mapper.MatchMapper;
import com.ProdeMaster.MatchService.infraestructure.entity.TeamEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JpaMatchRepository implements MatchRepository {
    private final SpringDataMatchRepository jpaRepository;
    private final MatchMapper mapper;

    private final SpringDataTeamRepository jpaTeamRepository;

    public JpaMatchRepository(SpringDataMatchRepository jpaRepository, MatchMapper mapper,
            SpringDataTeamRepository jpaTeamRepository) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.jpaTeamRepository = jpaTeamRepository;
    }

    @Override
    public Match save(Match match) {

        Map<String, Long> teamsNames = resolveTeamIds(match);

        if (teamsNames.get(match.getHomeTeam()) == null || teamsNames.get(match.getAwayTeam()) == null) {
            throw new IllegalArgumentException("Teams not found");
        }

        MatchEntity savedEntity = jpaRepository
                .save(mapper.toEntity(match, teamsNames.get(match.getHomeTeam()), teamsNames.get(match.getAwayTeam())));
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Match> findById(Long matchId) {
        if (matchId == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
        return jpaRepository.findById(matchId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Match> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long matchId) {
        if (matchId == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
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
        Long statusId = MatchMapper.mapMatchStatusToStatusId(status);
        return jpaRepository.findByStatusId(statusId).stream()
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
        if (matchId == null) {
            throw new IllegalArgumentException("Match ID cannot be null");
        }
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
        entity.setStatusId(MatchMapper.mapMatchStatusToStatusId(status));
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
     * Helper method to get TeamID from name
     */
    @SuppressWarnings("unused")
    private Long getTeamIdFromName(String nameTeam) {
        try {
            return jpaTeamRepository.findByName(nameTeam).map(TeamEntity::getId).orElseThrow();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return 0L;
    }

    /**
     * Helper method to get TeamID from name
     * ESTE EMTODO PODRIA IR EN UNA CLASE SEPARADA
     */
    private Map<String, Long> resolveTeamIds(Match match) {
        List<String> names = List.of(
                match.getHomeTeam(),
                match.getAwayTeam());

        return jpaTeamRepository.findIdsByNames(names);
    }
}
