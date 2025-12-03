package com.ProdeMaster.MatchService.application.service;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.domain.model.Match;
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

    public MatchApplicationService(MatchRepository matchRepository, MatchCacheRepository cacheRepository,
            MatchEventPublisher eventPublisher) {
        this.matchRepository = matchRepository;
        this.cacheRepository = cacheRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Match createMatch(Match match) {
        Match savedMatch = matchRepository.save(match);
        cacheRepository.cacheMatch(savedMatch);
        publishMatchUpdate(savedMatch, "Partido creado");
        return savedMatch;
    }

    @Override
    public Optional<Match> getMatch(Long matchId) {
        return cacheRepository.getFromCache(matchId)
                .or(() -> matchRepository.findById(matchId)
                        .map(match -> {
                            cacheRepository.cacheMatch(match);
                            return match;
                        }));
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match updateMatchScore(Long matchId, Integer homeTeamScore, Integer awayTeamScore) {
        Optional<Match> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent() && canUpdateMatch(matchId)) {
            Match match = matchOpt.get();
            match.updateScore(homeTeamScore, awayTeamScore);

            matchRepository.updateMatchScore(matchId, homeTeamScore, awayTeamScore);
            cacheRepository.updateCachedMatch(match);

            publishMatchUpdate(match, "Marcador actualizado");
            return match;
        }
        throw new IllegalStateException("No se puede actualizar el partido: " + matchId);
    }

    @Override
    public Match updateMatchStatus(Long matchId, MatchStatus newStatus) {
        Optional<Match> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent()) {
            Match match = matchOpt.get();

            switch (newStatus) {
                case IN_PROGRESS:
                    match.start();
                    break;
                case SUSPENDED:
                    match.suspend();
                    break;
                case FINISHED:
                    int home = match.getHomeTeamScore() != null ? match.getHomeTeamScore() : 0;
                    int away = match.getAwayTeamScore() != null ? match.getAwayTeamScore() : 0;
                    match.finish(home, away);
                    break;
                case SCHEDULED:
                    if (match.getStatus() == MatchStatus.SUSPENDED) {
                        match.reschedule(match.getMatchDateTime());
                    } else if (match.getStatus() != MatchStatus.SCHEDULED) {
                        throw new IllegalStateException(
                                "El partido no puede ser reprogramado. Estado actual: " + match.getStatus());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Estado no soportado: " + newStatus);
            }

            matchRepository.updateMatchStatus(matchId, newStatus);
            cacheRepository.updateCachedMatch(match);

            publishMatchUpdate(match, "Estado actualizado a: " + newStatus);
            return match;
        }
        throw new IllegalStateException("Partido no encontrado: " + matchId);
    }

    @Override
    public Match confirmMatchResult(Long matchId) {
        Optional<Match> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent()) {
            Match match = matchOpt.get();
            if (match.getStatus() != MatchStatus.FINISHED) {
                int home = match.getHomeTeamScore() != null ? match.getHomeTeamScore() : 0;
                int away = match.getAwayTeamScore() != null ? match.getAwayTeamScore() : 0;
                match.finish(home, away);
            }
            match.confirmResult();

            matchRepository.confirmMatchResult(matchId);
            cacheRepository.updateCachedMatch(match);

            publishMatchUpdate(match, "Resultado confirmado");
            return match;
        }
        throw new IllegalStateException("No se puede confirmar el resultado del partido: " + matchId);
    }

    @Override
    public List<Match> getMatchesByLeague(String league) {
        return matchRepository.findByLeague(league);
    }

    @Override
    public List<Match> getMatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return matchRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Match> getMatchesByStatus(MatchStatus status) {
        return matchRepository.findByStatus(status);
    }

    @Override
    public List<Match> getMatchesByTeam(String teamName) {
        return matchRepository.findByTeam(teamName);
    }

    @Override
    public boolean isMatchExist(Long matchId) {
        return matchRepository.existsById(matchId);
    }

    @Override
    public boolean canUpdateMatch(Long matchId) {
        Optional<Match> match = getMatch(matchId);
        return match.map(m -> !MatchStatus.FINISHED.equals(m.getStatus()))
                .orElse(false);
    }

    private void publishMatchUpdate(Match match, String description) {
        MatchUpdatedEvent event = new MatchUpdatedEvent(
                match.getMatchId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getHomeTeamScore(),
                match.getAwayTeamScore(),
                match.getStatus(),
                LocalDateTime.now(),
                description);
        eventPublisher.publish(event);
    }
}