package com.ProdeMaster.MatchService.domain.model;

import com.ProdeMaster.MatchService.domain.exception.InvalidMatchStateTransitionException;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Match implements Serializable {
    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private String league;
    private LocalDateTime matchDateTime;
    private MatchStatus status;
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
        this.status = MatchStatus.SCHEDULED;
        this.homeTeamScore = null;
        this.awayTeamScore = null;
        this.resultConfirmed = false;
    }

    // Business Methods for State Transitions

    public void start() {
        if (this.status != MatchStatus.SCHEDULED) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede comenzar. Estado actual: " + this.status);
        }
        this.status = MatchStatus.IN_PROGRESS;
        this.homeTeamScore = 0;
        this.awayTeamScore = 0;
    }

    public void suspend() {
        if (this.status == MatchStatus.FINISHED) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede ser suspendido. Estado actual: " + this.status);
        }
        this.status = MatchStatus.SUSPENDED;
    }

    public void finish(Integer homeTeamScore, Integer awayTeamScore) {
        if (this.status != MatchStatus.IN_PROGRESS) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede terminar. Estado actual: " + this.status);
        }
        validateScores(homeTeamScore, awayTeamScore);

        this.status = MatchStatus.FINISHED;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    public void reschedule(LocalDateTime newDate) {
        if (this.status != MatchStatus.SUSPENDED) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede ser reprogramado. Estado actual: " + this.status);
        }
        if (newDate == null || newDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de reprogramación no es valida");
        }
        this.matchDateTime = newDate;
        this.status = MatchStatus.SCHEDULED;
        this.homeTeamScore = null;
        this.awayTeamScore = null;
    }

    public void updateSuspendedInfo() {
        if (this.status != MatchStatus.SUSPENDED) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede ser actualizado. Estado actual: " + this.status);
        }
        // TODO: Update suspended info
        this.status = MatchStatus.SUSPENDED;
    }

    public void changeSchedule(LocalDateTime newDate) {
        if (this.status != MatchStatus.SCHEDULED) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede ser cambiado. Estado actual: " + this.status);
        }
        if (newDate == null || newDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de cambio debe ser en el futuro");
        }
        this.matchDateTime = newDate;
    }

    public void updateScore(Integer homeTeamScore, Integer awayTeamScore) {
        if (this.status != MatchStatus.IN_PROGRESS) {
            throw new InvalidMatchStateTransitionException(
                    "El partido no puede ser actualizado. Estado actual: " + this.status);
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
        if (this.status != MatchStatus.FINISHED) {
            throw new InvalidMatchStateTransitionException(
                    "El resultado no puede ser confirmado. Estado actual: " + this.status);
        }
        this.resultConfirmed = true;
    }

    private void validateScores(Integer home, Integer away) {
        if (home == null || away == null) {
            throw new IllegalArgumentException("Los marcadores no pueden ser nulos");
        }
        if (home < 0 || away < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos");
        }
    }

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