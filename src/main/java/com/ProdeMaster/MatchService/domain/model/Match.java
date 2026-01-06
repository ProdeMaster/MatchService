package com.ProdeMaster.MatchService.domain.model;

import com.ProdeMaster.MatchService.domain.exception.InvalidMatchStateTransitionException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.EnumSet;

public class Match implements Serializable {
    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private String league;
    private LocalDateTime matchDateTime;
    private MatchStatus status;
    private MatchStatus previousStatus;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Boolean resultConfirmed;

    public Match() {
    }

    public Match(Long matchId, String homeTeam, String awayTeam, String league, LocalDateTime matchDateTime) {
        if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) {
            throw new IllegalArgumentException("Los equipos no pueden estar vacíos");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo");
        }
        if (matchDateTime == null || matchDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del partido no es valida");
        }

        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.league = league;
        this.matchDateTime = matchDateTime;
        this.status = MatchStatus.NS;
        this.previousStatus = null;
        this.homeTeamScore = null;
        this.awayTeamScore = null;
        this.resultConfirmed = false;
    }

    /**
     * Recommended method to avoid breaking historical data and unwanted exceptions
     * Used only by MatchMapper
     */
    public static Match reconstitute(Long matchId, String homeTeam, String awayTeam, String league,
            LocalDateTime matchDateTime, MatchStatus status, MatchStatus previousStatus,
            Integer homeTeamScore, Integer awayTeamScore, Boolean resultConfirmed) {
        Match match = new Match();
        match.matchId = matchId;
        match.homeTeam = homeTeam;
        match.awayTeam = awayTeam;
        match.league = league;
        match.matchDateTime = matchDateTime;
        match.status = status;
        match.previousStatus = previousStatus;
        match.homeTeamScore = homeTeamScore;
        match.awayTeamScore = awayTeamScore;
        match.resultConfirmed = resultConfirmed;
        return match;
    }

    // ==================== INITIAL STATE TRANSITIONS ====================

    public void setToBeAnnounced() {
        validateTransition(MatchStatus.TBA, EnumSet.of(MatchStatus.PENDING));
        this.status = MatchStatus.TBA;
    }

    public void setNotStarted() {
        validateTransition(MatchStatus.NS, EnumSet.of(MatchStatus.PENDING, MatchStatus.TBA, MatchStatus.POSTPONED,
                MatchStatus.DELAYED));
        this.status = MatchStatus.NS;
    }

    // ==================== NORMAL MATCH FLOW ====================

    public void startFirstHalf() {
        validateTransition(MatchStatus.INPLAY_1ST_HALF,
                EnumSet.of(MatchStatus.NS, MatchStatus.DELAYED, MatchStatus.SUSPENDED,
                        MatchStatus.INTERRUPTED));
        this.status = MatchStatus.INPLAY_1ST_HALF;
        if (this.homeTeamScore == null) {
            this.homeTeamScore = 0;
            this.awayTeamScore = 0;
        }
    }

    public void endFirstHalf() {
        validateTransition(MatchStatus.HT, EnumSet.of(MatchStatus.INPLAY_1ST_HALF));
        this.status = MatchStatus.HT;
    }

    public void startSecondHalf() {
        validateTransition(MatchStatus.INPLAY_2ND_HALF,
                EnumSet.of(MatchStatus.HT, MatchStatus.SUSPENDED, MatchStatus.INTERRUPTED));
        this.status = MatchStatus.INPLAY_2ND_HALF;
    }

    public void endRegularTime() {
        validateTransition(MatchStatus.FT, EnumSet.of(MatchStatus.INPLAY_2ND_HALF));
        this.status = MatchStatus.FT;
    }

    // ==================== EXTRA TIME FLOW ====================

    public void startExtraTime() {
        validateTransition(MatchStatus.INPLAY_ET,
                EnumSet.of(MatchStatus.FT, MatchStatus.SUSPENDED, MatchStatus.INTERRUPTED));
        this.status = MatchStatus.INPLAY_ET;
    }

    public void breakExtraTime() {
        validateTransition(MatchStatus.EXTRA_TIME_BREAK, EnumSet.of(MatchStatus.INPLAY_ET));
        this.status = MatchStatus.EXTRA_TIME_BREAK;
    }

    public void startExtraTimeSecondHalf() {
        validateTransition(MatchStatus.INPLAY_ET_2ND_HALF,
                EnumSet.of(MatchStatus.EXTRA_TIME_BREAK, MatchStatus.SUSPENDED,
                        MatchStatus.INTERRUPTED));
        this.status = MatchStatus.INPLAY_ET_2ND_HALF;
    }

    public void endExtraTime() {
        validateTransition(MatchStatus.AET, EnumSet.of(MatchStatus.INPLAY_ET_2ND_HALF));
        this.status = MatchStatus.AET;
    }

    // ==================== PENALTIES FLOW ====================

    public void startPenalties() {
        validateTransition(MatchStatus.INPLAY_PENALTIES, EnumSet.of(MatchStatus.AET, MatchStatus.SUSPENDED,
                MatchStatus.INTERRUPTED));
        this.status = MatchStatus.INPLAY_PENALTIES;
    }

    public void breakPenalties() {
        validateTransition(MatchStatus.PEN_BREAK, EnumSet.of(MatchStatus.INPLAY_PENALTIES));
        this.status = MatchStatus.PEN_BREAK;
    }

    public void endPenalties() {
        validateTransition(MatchStatus.FT_PEN, EnumSet.of(MatchStatus.PEN_BREAK));
        this.status = MatchStatus.FT_PEN;
    }

    // ==================== ADMINISTRATIVE - DELAYS ====================

    public void delay() {
        validateTransition(MatchStatus.DELAYED, EnumSet.of(MatchStatus.NS));
        this.status = MatchStatus.DELAYED;
    }

    public void postpone() {
        validateTransition(MatchStatus.POSTPONED, EnumSet.of(MatchStatus.NS));
        this.status = MatchStatus.POSTPONED;
    }

    // ==================== ADMINISTRATIVE - SUSPENSIONS ====================

    public void suspend() {
        if (!isInPlayState()) {
            throw new InvalidMatchStateTransitionException(
                    "El partido solo puede ser suspendido durante el juego. Estado actual: " + this.status);
        }
        this.previousStatus = this.status;
        this.status = MatchStatus.SUSPENDED;
    }

    public void resumeFromSuspension() {
        validateTransition(this.previousStatus, EnumSet.of(MatchStatus.SUSPENDED));
        if (this.previousStatus == null || !isInPlayState(this.previousStatus)) {
            throw new InvalidMatchStateTransitionException(
                    "No se puede reanudar desde suspensión sin un estado previo válido");
        }
        this.status = this.previousStatus;
        this.previousStatus = null;
    }

    // ==================== ADMINISTRATIVE - INTERRUPTIONS ====================

    public void interrupt() {
        if (!isInPlayState()) {
            throw new InvalidMatchStateTransitionException(
                    "El partido solo puede ser interrumpido durante el juego. Estado actual: " + this.status);
        }
        this.previousStatus = this.status;
        this.status = MatchStatus.INTERRUPTED;
    }

    public void resumeFromInterruption() {
        validateTransition(this.previousStatus, EnumSet.of(MatchStatus.INTERRUPTED));
        if (this.previousStatus == null || !isInPlayState(this.previousStatus)) {
            throw new InvalidMatchStateTransitionException(
                    "No se puede reanudar desde interrupción sin un estado previo válido");
        }
        this.status = this.previousStatus;
        this.previousStatus = null;
    }

    // ==================== ADMINISTRATIVE RESULTS ====================

    public void awardResult(Integer homeTeamScore, Integer awayTeamScore) {
        validateTransition(MatchStatus.AWARDED, EnumSet.of(MatchStatus.NS));
        validateScores(homeTeamScore, awayTeamScore);
        this.status = MatchStatus.AWARDED;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    public void declareWalkOver(Integer homeTeamScore, Integer awayTeamScore) {
        validateTransition(MatchStatus.WO, EnumSet.of(MatchStatus.NS));
        validateScores(homeTeamScore, awayTeamScore);
        this.status = MatchStatus.WO;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    // ==================== TERMINAL ADMINISTRATIVE STATES ====================

    public void cancel() {
        validateTransition(MatchStatus.CANCELLED, EnumSet.of(MatchStatus.NS));
        this.status = MatchStatus.CANCELLED;
    }

    public void abandon() {
        if (isTerminalState()) {
            throw new InvalidMatchStateTransitionException(
                    "No se puede abandonar un partido que ya finalizó. Estado actual: " + this.status);
        }
        this.status = MatchStatus.ABANDONED;
    }

    public void delete() {
        this.status = MatchStatus.DELETED;
    }

    public void markAwaitingUpdates() {
        this.previousStatus = this.status;
        this.status = MatchStatus.AWAITING_UPDATES;
    }

    public void resumeFromAwaitingUpdates() {
        validateTransition(this.previousStatus, EnumSet.of(MatchStatus.AWAITING_UPDATES));
        if (this.previousStatus == null) {
            throw new InvalidMatchStateTransitionException(
                    "No se puede reanudar desde AWAITING_UPDATES sin un estado previo");
        }
        this.status = this.previousStatus;
        this.previousStatus = null;
    }

    // ==================== SCORE MANAGEMENT ====================

    public void updateScore(Integer homeTeamScore, Integer awayTeamScore) {
        if (!isInPlayState()) {
            throw new InvalidMatchStateTransitionException(
                    "El marcador solo puede actualizarse durante el juego. Estado actual: " + this.status);
        }
        validateScores(homeTeamScore, awayTeamScore);

        if (this.homeTeamScore != null && homeTeamScore < this.homeTeamScore) {
            throw new IllegalArgumentException("El marcador del equipo local no puede disminuir");
        }
        if (this.awayTeamScore != null && awayTeamScore < this.awayTeamScore) {
            throw new IllegalArgumentException("El marcador del equipo visitante no puede disminuir");
        }

        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    public void confirmResult() {
        if (!isTerminalState()) {
            throw new InvalidMatchStateTransitionException(
                    "El resultado solo puede confirmarse cuando el partido ha finalizado. Estado actual: "
                            + this.status);
        }
        this.resultConfirmed = true;
    }

    // ==================== SCHEDULE MANAGEMENT ====================

    public void changeSchedule(LocalDateTime newDate) {
        if (this.status != MatchStatus.NS && this.status != MatchStatus.TBA && this.status != MatchStatus.PENDING) {
            throw new InvalidMatchStateTransitionException(
                    "Solo se puede cambiar la fecha de partidos no iniciados. Estado actual: " + this.status);
        }
        if (newDate == null || newDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de cambio debe ser en el futuro");
        }
        this.matchDateTime = newDate;
    }

    // ==================== HELPER METHODS ====================
    private void validateTransition(MatchStatus newStatus, EnumSet<MatchStatus> allowedCurrentStates) {
        if (!allowedCurrentStates.contains(this.status)) {
            throw new InvalidMatchStateTransitionException(
                    String.format("Transición inválida de %s a %s", this.status, newStatus));
        }
    }

    private boolean isInPlayState() {
        return isInPlayState(this.status);
    }

    private boolean isInPlayState(MatchStatus status) {
        return status == MatchStatus.INPLAY_1ST_HALF ||
                status == MatchStatus.INPLAY_2ND_HALF ||
                status == MatchStatus.INPLAY_ET ||
                status == MatchStatus.INPLAY_ET_2ND_HALF ||
                status == MatchStatus.INPLAY_PENALTIES;
    }

    private boolean isTerminalState() {
        return this.status == MatchStatus.FT ||
                this.status == MatchStatus.AET ||
                this.status == MatchStatus.FT_PEN ||
                this.status == MatchStatus.AWARDED ||
                this.status == MatchStatus.WO ||
                this.status == MatchStatus.CANCELLED ||
                this.status == MatchStatus.ABANDONED ||
                this.status == MatchStatus.DELETED;
    }

    private void validateScores(Integer home, Integer away) {
        if (home == null || away == null) {
            throw new IllegalArgumentException("Los marcadores no pueden ser nulos");
        }
        if (home < 0 || away < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos");
        }
    }

    // ==================== GETTERS ====================

    public Long getMatchId() {
        return matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getLeague() {
        return league;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public MatchStatus getPreviousStatus() {
        return previousStatus;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public Boolean isResultConfirmed() {
        return resultConfirmed;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
}