package com.ProdeMaster.MatchService.application.service;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.application.port.out.cache.MatchCacheRepository;
import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchApplicationServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchCacheRepository cacheRepository;

    @Mock
    private MatchEventPublisher eventPublisher;

    private MatchApplicationService matchApplicationService;

    @BeforeEach
    void setUp() {
        matchApplicationService = new MatchApplicationService(matchRepository, cacheRepository, eventPublisher);
    }

    // ==================== CREATE MATCH ====================
    @Test
    void testCreateMatchSuccess() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        Match savedMatch = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));

        savedMatch.setMatchId(1L);

        when(matchRepository.save(match)).thenReturn(savedMatch);
        when(cacheRepository.cacheMatch(any(Match.class))).thenReturn(Mono.empty());

        Match result = matchApplicationService.createMatch(match);

        assertNotNull(result);
        assertEquals(1L, result.getMatchId());
        verify(matchRepository).save(match);
        verify(cacheRepository).cacheMatch(savedMatch);
        verify(eventPublisher).publish(any(MatchUpdatedEvent.class));
    }

    // ==================== GET MATCH ====================
    @Test
    void testGetMatchFromCache() {
        Match cachedMatch = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        cachedMatch.setMatchId(1L);

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(cachedMatch));

        Optional<Match> result = matchApplicationService.getMatch(1L);

        assertTrue(result.isPresent());
        assertEquals("Home Team", result.get().getHomeTeam());
        verify(cacheRepository).getMatch(1L);
        verify(matchRepository, never()).findById(any());
    }

    @Test
    void testGetMatchFromDatabaseWhenNotCached() {
        Match dbMatch = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        dbMatch.setMatchId(1L);

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.empty());
        when(matchRepository.findById(1L)).thenReturn(Optional.of(dbMatch));
        when(cacheRepository.cacheMatch(any(Match.class))).thenReturn(Mono.empty());

        Optional<Match> result = matchApplicationService.getMatch(1L);

        assertTrue(result.isPresent());
        assertEquals("Home Team", result.get().getHomeTeam());
        verify(cacheRepository).getMatch(1L);
        verify(matchRepository).findById(1L);
        verify(cacheRepository).cacheMatch(dbMatch);
    }

    @Test
    void testGetMatchNotFound() {
        when(cacheRepository.getMatch(999L)).thenReturn(Mono.empty());
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Match> result = matchApplicationService.getMatch(999L);

        assertFalse(result.isPresent());
    }

    // ==================== GET ALL MATCHES ====================
    @Test
    void testGetAllMatches() {
        List<Match> matches = Arrays.asList(
                new Match(1L, "Home1", "Away1", "League1", LocalDateTime.now().plusDays(1)),
                new Match(2L, "Home2", "Away2", "League2", LocalDateTime.now().plusDays(2)));
        matches.get(0).setMatchId(1L);
        matches.get(1).setMatchId(2L);

        when(matchRepository.findAll()).thenReturn(matches);

        List<Match> result = matchApplicationService.getAllMatches();

        assertEquals(2, result.size());
        verify(matchRepository).findAll();
    }

    // ==================== UPDATE SCORE ====================
    @Test
    void testUpdateMatchScoreSuccess() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));
        doNothing().when(matchRepository).updateMatchScore(1L, 2, 1);
        when(cacheRepository.updateCachedMatch(any(Match.class))).thenReturn(Mono.empty());

        Match result = matchApplicationService.updateMatchScore(1L, 2, 1);

        assertEquals(2, result.getHomeTeamScore());
        assertEquals(1, result.getAwayTeamScore());
        verify(matchRepository).updateMatchScore(1L, 2, 1);
        verify(cacheRepository).updateCachedMatch(any(Match.class));
        verify(eventPublisher).publish(any(MatchUpdatedEvent.class));
    }

    @Test
    void testUpdateMatchScoreMatchNotFound() {
        when(cacheRepository.getMatch(999L)).thenReturn(Mono.empty());
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> matchApplicationService.updateMatchScore(999L, 2, 1));
    }

    @Test
    void testUpdateMatchScoreCannotUpdateFinishedMatch() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));

        assertThrows(IllegalStateException.class, () -> matchApplicationService.updateMatchScore(1L, 2, 1));
    }

    // ==================== UPDATE STATUS ====================
    @Test
    void testUpdateMatchStatusSuccess() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));
        doNothing().when(matchRepository).updateMatchStatus(1L, MatchStatus.INPLAY_1ST_HALF);
        when(cacheRepository.updateCachedMatch(any(Match.class))).thenReturn(Mono.empty());

        Match result = matchApplicationService.updateMatchStatus(1L, MatchStatus.INPLAY_1ST_HALF);

        assertEquals(MatchStatus.INPLAY_1ST_HALF, result.getStatus());
        verify(matchRepository).updateMatchStatus(1L, MatchStatus.INPLAY_1ST_HALF);
        verify(eventPublisher).publish(any(MatchUpdatedEvent.class));
    }

    @Test
    void testUpdateMatchStatusInvalidTransition() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));

        assertThrows(IllegalStateException.class,
                () -> matchApplicationService.updateMatchStatus(1L, MatchStatus.INPLAY_1ST_HALF));
    }

    @Test
    void testUpdateMatchStatusNotFound() {
        when(cacheRepository.getMatch(999L)).thenReturn(Mono.empty());
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> matchApplicationService.updateMatchStatus(999L, MatchStatus.INPLAY_1ST_HALF));
    }

    // ==================== CONFIRM RESULT ====================
    @Test
    void testConfirmMatchResultSuccess() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));
        doNothing().when(matchRepository).confirmMatchResult(1L);
        when(cacheRepository.updateCachedMatch(any(Match.class))).thenReturn(Mono.empty());

        Match result = matchApplicationService.confirmMatchResult(1L);

        assertTrue(result.isResultConfirmed());
        verify(matchRepository).confirmMatchResult(1L);
    }

    @Test
    void testConfirmMatchResultNotFound() {
        when(cacheRepository.getMatch(999L)).thenReturn(Mono.empty());
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> matchApplicationService.confirmMatchResult(999L));
    }

    // ==================== QUERY OPERATIONS ====================
    @Test
    void testGetMatchesByLeague() {
        List<Match> matches = Arrays.asList(
                new Match(1L, "Home1", "Away1", "Premier League", LocalDateTime.now().plusDays(1)),
                new Match(2L, "Home2", "Away2", "Premier League", LocalDateTime.now().plusDays(2)));
        matches.get(0).setMatchId(1L);
        matches.get(1).setMatchId(2L);

        when(matchRepository.findByLeague("Premier League")).thenReturn(matches);

        List<Match> result = matchApplicationService.getMatchesByLeague("Premier League");

        assertEquals(2, result.size());
        verify(matchRepository).findByLeague("Premier League");
    }

    @Test
    void testGetMatchesByDateRange() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(7);
        List<Match> matches = Arrays.asList(
                new Match(1L, "Home1", "Away1", "League1", start.plusDays(1)));
        matches.get(0).setMatchId(1L);

        when(matchRepository.findByDateRange(start, end)).thenReturn(matches);

        List<Match> result = matchApplicationService.getMatchesByDateRange(start, end);

        assertEquals(1, result.size());
        verify(matchRepository).findByDateRange(start, end);
    }

    @Test
    void testGetMatchesByStatus() {
        Match match = new Match(1L, "Home1", "Away1", "League1", LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();

        when(matchRepository.findByStatus(MatchStatus.INPLAY_1ST_HALF)).thenReturn(Arrays.asList(match));

        List<Match> result = matchApplicationService.getMatchesByStatus(MatchStatus.INPLAY_1ST_HALF);

        assertEquals(1, result.size());
        verify(matchRepository).findByStatus(MatchStatus.INPLAY_1ST_HALF);
    }

    @Test
    void testGetMatchesByTeam() {
        Match match = new Match(1L, "Home Team", "Away Team", "League", LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);

        when(matchRepository.findByTeam("Home Team")).thenReturn(Arrays.asList(match));
        when(matchRepository.findByTeam("Away Team")).thenReturn(Arrays.asList(match));

        List<Match> resultHomeTeam = matchApplicationService.getMatchesByTeam("Home Team");
        List<Match> resultAwayTeam = matchApplicationService.getMatchesByTeam("Away Team");

        assertEquals(1, resultHomeTeam.size());
        assertEquals(1, resultAwayTeam.size());
        verify(matchRepository).findByTeam("Home Team");
        verify(matchRepository).findByTeam("Away Team");
    }

    // ==================== VALIDATION OPERATIONS ====================
    @Test
    void testIsMatchExist() {
        when(matchRepository.existsById(1L)).thenReturn(true);
        when(matchRepository.existsById(999L)).thenReturn(false);

        assertTrue(matchApplicationService.isMatchExist(1L));
        assertFalse(matchApplicationService.isMatchExist(999L));
    }

    @Test
    void testCanUpdateMatchInProgress() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));

        assertTrue(matchApplicationService.canUpdateMatch(1L));
    }

    @Test
    void testCanUpdateMatchFinished() {
        Match match = new Match(1L, "Home Team", "Away Team", "Premier League",
                LocalDateTime.now().plusDays(1));
        match.setMatchId(1L);
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();

        when(cacheRepository.getMatch(1L)).thenReturn(Mono.just(match));

        assertFalse(matchApplicationService.canUpdateMatch(1L));
    }

    @Test
    void testCanUpdateMatchNotFound() {
        when(cacheRepository.getMatch(999L)).thenReturn(Mono.empty());
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        assertFalse(matchApplicationService.canUpdateMatch(999L));
    }
}
