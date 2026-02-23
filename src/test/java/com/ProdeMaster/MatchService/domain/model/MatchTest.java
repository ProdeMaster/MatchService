package com.ProdeMaster.MatchService.domain.model;

import com.ProdeMaster.MatchService.domain.exception.InvalidMatchStateTransitionException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void testInitialStateIsNotStarted() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        assertEquals(MatchStatus.NS, match.getStatus());
    }

    @Test
    void testConstructorValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new Match(1L, "", "Away", "League", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class,
                () -> new Match(1L, "Home", "Home", "League", LocalDateTime.now().plusDays(1)));
        assertThrows(IllegalArgumentException.class,
                () -> new Match(1L, "Home", "Away", "League", LocalDateTime.now().minusDays(1)));
    }

    // ==================== INITIAL STATE TRANSITIONS ====================
    @Test
    void testSetToBeAnnounced() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setToBeAnnounced();
        assertEquals(MatchStatus.TBA, match.getStatus());
    }

    @Test
    void testSetNotStartedFromPending() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        assertEquals(MatchStatus.NS, match.getStatus());
    }

    @Test
    void testSetNotStartedFromTBA() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setToBeAnnounced();
        match.setNotStarted();
        assertEquals(MatchStatus.NS, match.getStatus());
    }

    // ==================== NORMAL MATCH FLOW ====================
    @Test
    void testStartFirstHalf() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        assertEquals(MatchStatus.INPLAY_1ST_HALF, match.getStatus());
        assertEquals(0, match.getHomeTeamScore());
        assertEquals(0, match.getAwayTeamScore());
    }

    @Test
    void testEndFirstHalf() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        assertEquals(MatchStatus.HT, match.getStatus());
    }

    @Test
    void testStartSecondHalf() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        assertEquals(MatchStatus.INPLAY_2ND_HALF, match.getStatus());
    }

    @Test
    void testEndRegularTime() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();
        assertEquals(MatchStatus.FT, match.getStatus());
    }

    // ==================== EXTRA TIME FLOW ====================
    @Test
    void testExtraTimeFlow() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();

        match.startExtraTime();
        assertEquals(MatchStatus.INPLAY_ET, match.getStatus());

        match.breakExtraTime();
        assertEquals(MatchStatus.EXTRA_TIME_BREAK, match.getStatus());

        match.startExtraTimeSecondHalf();
        assertEquals(MatchStatus.INPLAY_ET_2ND_HALF, match.getStatus());

        match.endExtraTime();
        assertEquals(MatchStatus.AET, match.getStatus());
    }

    // ==================== PENALTIES FLOW ====================
    @Test
    void testPenaltiesFlow() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();
        match.startExtraTime();
        match.breakExtraTime();
        match.startExtraTimeSecondHalf();
        match.endExtraTime();

        match.startPenalties();
        assertEquals(MatchStatus.INPLAY_PENALTIES, match.getStatus());

        match.breakPenalties();
        assertEquals(MatchStatus.PEN_BREAK, match.getStatus());

        match.endPenalties();
        assertEquals(MatchStatus.FT_PEN, match.getStatus());
    }

    // ==================== ADMINISTRATIVE - DELAYS ====================
    @Test
    void testDelay() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.delay();
        assertEquals(MatchStatus.DELAYED, match.getStatus());
    }

    @Test
    void testPostpone() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.postpone();
        assertEquals(MatchStatus.POSTPONED, match.getStatus());
    }

    @Test
    void testResumeFromPostponed() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.postpone();
        match.setNotStarted();
        assertEquals(MatchStatus.NS, match.getStatus());
    }

    // ==================== ADMINISTRATIVE - SUSPENSIONS ====================
    @Test
    void testSuspendFromInProgress() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.suspend();
        assertEquals(MatchStatus.SUSPENDED, match.getStatus());
        assertEquals(MatchStatus.INPLAY_1ST_HALF, match.getPreviousStatus());
    }

    @Test
    void testResumeFromSuspension() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.suspend();
        match.resumeFromSuspension();
        assertEquals(MatchStatus.INPLAY_1ST_HALF, match.getStatus());
        assertNull(match.getPreviousStatus());
    }

    @Test
    void testSuspendFromNonInProgressFails() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        assertThrows(InvalidMatchStateTransitionException.class, match::suspend);
    }

    // ==================== ADMINISTRATIVE - INTERRUPTIONS ====================
    @Test
    void testInterruptFromInProgress() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.interrupt();
        assertEquals(MatchStatus.INTERRUPTED, match.getStatus());
        assertEquals(MatchStatus.INPLAY_1ST_HALF, match.getPreviousStatus());
    }

    @Test
    void testResumeFromInterruption() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.interrupt();
        match.resumeFromInterruption();
        assertEquals(MatchStatus.INPLAY_1ST_HALF, match.getStatus());
        assertNull(match.getPreviousStatus());
    }

    // ==================== ADMINISTRATIVE RESULTS ====================

    @Test
    void testAwardResult() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.awardResult(3, 0);
        assertEquals(MatchStatus.AWARDED, match.getStatus());
        assertEquals(3, match.getHomeTeamScore());
        assertEquals(0, match.getAwayTeamScore());
    }

    @Test
    void testDeclareWalkOver() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.declareWalkOver(3, 0);
        assertEquals(MatchStatus.WO, match.getStatus());
        assertEquals(3, match.getHomeTeamScore());
        assertEquals(0, match.getAwayTeamScore());
    }

    // ==================== TERMINAL ADMINISTRATIVE STATES ====================
    @Test
    void testCancel() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.cancel();
        assertEquals(MatchStatus.CANCELLED, match.getStatus());
    }

    @Test
    void testAbandon() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.abandon();
        assertEquals(MatchStatus.ABANDONED, match.getStatus());
    }

    @Test
    void testDelete() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.delete();
        assertEquals(MatchStatus.DELETED, match.getStatus());
    }

    @Test
    void testAwaitingUpdates() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.markAwaitingUpdates();
        assertEquals(MatchStatus.AWAITING_UPDATES, match.getStatus());
        assertEquals(MatchStatus.NS, match.getPreviousStatus());
    }

    @Test
    void testResumeFromAwaitingUpdates() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.markAwaitingUpdates();
        match.resumeFromAwaitingUpdates();
        assertEquals(MatchStatus.NS, match.getStatus());
        assertNull(match.getPreviousStatus());
    }

    // ==================== SCORE MANAGEMENT ====================
    @Test
    void testUpdateScoreOnlyInProgress() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        assertThrows(InvalidMatchStateTransitionException.class, () -> match.updateScore(1, 0));

        match.startFirstHalf();
        match.updateScore(1, 0);
        assertEquals(1, match.getHomeTeamScore());
        assertEquals(0, match.getAwayTeamScore());
    }

    @Test
    void testUpdateScoreNonDecreasing() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.updateScore(1, 0);
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(0, 0));
    }

    @Test
    void testUpdateScoreInvalidValues() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> match.updateScore(null, 0));
    }

    @Test
    void testConfirmResultOnlyFinished() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        assertThrows(InvalidMatchStateTransitionException.class, match::confirmResult);

        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();
        match.confirmResult();
        assertTrue(match.isResultConfirmed());
    }

    // ==================== SCHEDULE MANAGEMENT ====================
    @Test
    void testChangeSchedule() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        LocalDateTime newDate = LocalDateTime.now().plusDays(2);
        match.changeSchedule(newDate);
        assertEquals(MatchStatus.NS, match.getStatus());
        assertEquals(newDate, match.getMatchDateTime());
    }

    @Test
    void testChangeScheduleInvalidDate() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        assertThrows(IllegalArgumentException.class,
                () -> match.changeSchedule(LocalDateTime.now().minusDays(1)));
    }

    @Test
    void testChangeScheduleOnlyWhenNotStarted() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        assertThrows(InvalidMatchStateTransitionException.class,
                () -> match.changeSchedule(LocalDateTime.now().plusDays(2)));
    }

    // ==================== INVALID TRANSITIONS ====================
    @Test
    void testInvalidTransitionStartFromFinished() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();
        assertThrows(InvalidMatchStateTransitionException.class, match::startFirstHalf);
    }

    @Test
    void testInvalidTransitionEndFirstHalfWithoutStart() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        assertThrows(InvalidMatchStateTransitionException.class, match::endFirstHalf);
    }

    @Test
    void testCannotAbandonFinishedMatch() {
        Match match = new Match(1L, "Home", "Away", "League", LocalDateTime.now().plusDays(1));
        match.setNotStarted();
        match.startFirstHalf();
        match.endFirstHalf();
        match.startSecondHalf();
        match.endRegularTime();
        assertThrows(InvalidMatchStateTransitionException.class, match::abandon);
    }
}
