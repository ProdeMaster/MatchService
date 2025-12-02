package com.ProdeMaster.MatchService.application.service;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.domain.model.MatchModel;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.application.port.out.cache.MatchCacheRepository;
import com.ProdeMaster.MatchService.application.port.in.web.MatchService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MatchApplicationService implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchCacheRepository cacheRepository;
    private final MatchEventPublisher eventPublisher;

    public MatchApplicationService(MatchRepository matchRepository, MatchCacheRepository cacheRepository, MatchEventPublisher eventPublisher) {
        this.matchRepository = matchRepository;
        this.cacheRepository = cacheRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public MatchModel createMatch(MatchModel match) {
        MatchModel savedMatch = matchRepository.save(match);
        cacheRepository.cacheMatch(savedMatch);
        publishMatchUpdate(savedMatch, "Partido creado");
        return savedMatch;
    }

    @Override
    public Optional<MatchModel> getMatch(Long matchId) {
        return cacheRepository.getFromCache(matchId)
                .or(() -> matchRepository.findById(matchId)
                        .map(match -> {
                            cacheRepository.cacheMatch(match);
                            return match;
                        }));
    }

    @Override
    public List<MatchModel> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public MatchModel updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore) {
        Optional<MatchModel> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent() && canUpdateMatch(matchId)) {
            MatchModel match = matchOpt.get();
            match.setHomeTeamScore(homeTeamScore);
            match.setAwayTeamScore(awayTeamScore);

            matchRepository.updateMatchScore(matchId, homeTeamScore, awayTeamScore);
            cacheRepository.updateCachedMatch(match);

            publishMatchUpdate(match, "Marcador actualizado");
            return match;
        }
        throw new IllegalStateException("No se puede actualizar el partido: " + matchId);
    }

    @Override
    public MatchModel updateMatchStatus(Long matchId, MatchStatus newStatus) {
        Optional<MatchModel> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent()) {
            MatchModel match = matchOpt.get();
            match.setStatus(newStatus);

            matchRepository.updateMatchStatus(matchId, newStatus);
            cacheRepository.updateCachedMatch(match);

            publishMatchUpdate(match, "Estado actualizado a: " + newStatus);
            return match;
        }
        throw new IllegalStateException("Partido no encontrado: " + matchId);
    }

    @Override
    public MatchModel confirmMatchResult(Long matchId) {
        Optional<MatchModel> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent()) {
            MatchModel match = matchOpt.get();
            match.setResultConfirmed(true);
            match.setStatus(MatchStatus.FINISHED);

            matchRepository.confirmMatchResult(matchId);
            cacheRepository.updateCachedMatch(match);

            publishMatchUpdate(match, "Resultado confirmado");
            return match;
        }
        throw new IllegalStateException("No se puede confirmar el resultado del partido: " + matchId);
    }

    @Override
    public List<MatchModel> getMatchesByLeague(String league) {
        return matchRepository.findByLeague(league);
    }

    @Override
    public List<MatchModel> getMatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return matchRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<MatchModel> getMatchesByStatus(MatchStatus status) {
        return matchRepository.findByStatus(status);
    }

    @Override
    public List<MatchModel> getMatchesByTeam(String teamName) {
        return matchRepository.findByTeam(teamName);
    }

    @Override
    public boolean isMatchExist(Long matchId) {
        return matchRepository.existsById(matchId);
    }

    @Override
    public boolean canUpdateMatch(Long matchId) {
        Optional<MatchModel> match = getMatch(matchId);
        return match.map(m -> !MatchStatus.FINISHED.equals(m.getStatus()))
                .orElse(false);
    }

    private void publishMatchUpdate(MatchModel match, String description) {
        MatchUpdatedEvent event = new MatchUpdatedEvent(
                match.getMatchId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getHomeTeamScore(),
                match.getAwayTeamScore(),
                match.getStatus(),
                LocalDateTime.now(),
                description
        );
        eventPublisher.publish(event);
    }
}