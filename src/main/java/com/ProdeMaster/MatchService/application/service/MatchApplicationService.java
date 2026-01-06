package com.ProdeMaster.MatchService.application.service;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.domain.exception.InvalidMatchStateTransitionException;
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

            try {
                switch (newStatus) {
                    // Initial states
                    case TBA:
                        match.setToBeAnnounced();
                        break;
                    case NS:
                        match.setNotStarted();
                        break;

                    // Normal match flow
                    case INPLAY_1ST_HALF:
                        match.startFirstHalf();
                        break;
                    case HT:
                        match.endFirstHalf();
                        break;
                    case INPLAY_2ND_HALF:
                        match.startSecondHalf();
                        break;
                    case FT:
                        match.endRegularTime();
                        break;

                    // Extra time
                    case INPLAY_ET:
                        match.startExtraTime();
                        break;
                    case EXTRA_TIME_BREAK:
                        match.breakExtraTime();
                        break;
                    case INPLAY_ET_2ND_HALF:
                        match.startExtraTimeSecondHalf();
                        break;
                    case AET:
                        match.endExtraTime();
                        break;

                    // Penalties
                    case INPLAY_PENALTIES:
                        match.startPenalties();
                        break;
                    case PEN_BREAK:
                        match.breakPenalties();
                        break;
                    case FT_PEN:
                        match.endPenalties();
                        break;

                    // Administrative - delays
                    case DELAYED:
                        match.delay();
                        break;
                    case POSTPONED:
                        match.postpone();
                        break;

                    // Administrative - suspensions/interruptions
                    case SUSPENDED:
                        match.suspend();
                        break;
                    case INTERRUPTED:
                        match.interrupt();
                        break;

                    // Administrative - terminal
                    case CANCELLED:
                        match.cancel();
                        break;
                    case ABANDONED:
                        match.abandon();
                        break;
                    case DELETED:
                        match.delete();
                        break;
                    case AWAITING_UPDATES:
                        match.markAwaitingUpdates();
                        break;

                    default:
                        throw new IllegalArgumentException("Estado no soportado: " + newStatus);
                }

                matchRepository.updateMatchStatus(matchId, newStatus);
                cacheRepository.updateCachedMatch(match);

                publishMatchUpdate(match, "Estado actualizado a: " + newStatus);
                return match;
            } catch (InvalidMatchStateTransitionException e) {
                throw new IllegalStateException("Transición de estado inválida: " + e.getMessage(), e);
            }
        }
        throw new IllegalStateException("Partido no encontrado: " + matchId);
    }

    @Override
    public Match confirmMatchResult(Long matchId) {
        Optional<Match> matchOpt = getMatch(matchId);
        if (matchOpt.isPresent()) {
            Match match = matchOpt.get();
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
        return match
                .map(m -> m.getStatus() != MatchStatus.FT && m.getStatus() != MatchStatus.AET
                        && m.getStatus() != MatchStatus.FT_PEN)
                .orElse(false);
    }

    // ========= MOVER A UNA CLASE PROPIA PARA EVENTOS ========
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